package de.tunetown.roommap.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import Jama.Matrix;
import edu.stanford.rsl.conrad.geometry.shapes.simple.PointND;
import edu.stanford.rsl.conrad.numerics.SimpleMatrix;
import edu.stanford.rsl.conrad.numerics.SimpleVector;
import edu.stanford.rsl.tutorial.motion.estimation.ThinPlateSplineInterpolation;

public class ThinPlateSplineInterpolationPooled extends ThinPlateSplineInterpolation {

	public ThinPlateSplineInterpolationPooled(int dimension, ArrayList<PointND> points, ArrayList<PointND> values) {
		super(dimension, points, values);
	}

	/**
	 * Multithreaded version of the super method
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void estimateCoefficients() {
		int n = gridPoints.size();
		int sizeL = dim * n + dim * dim + dim;
		A = new SimpleMatrix(dim, dim);
		b = new SimpleVector(dim);

		Matrix LJama = new Matrix(sizeL,sizeL);
		Matrix rhsJama = new Matrix(sizeL, 1);

		// Create new thread pool
		int chunkSize = n / (Runtime.getRuntime().availableProcessors() - 3);
		List<Future> futures = new ArrayList<Future>();
		ExecutorService pool = Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors() - 2);
		
		// Add workers to the pool
		ArrayList<Integer> chunk = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			chunk.add(i);
			if (chunk.size() >= chunkSize) {
				futures.add(pool.submit(new EstimateCoefficientsExecutor(this, chunk)));
				chunk = new ArrayList<Integer>();
			}
		}
		if (chunk.size() > 0) {
			futures.add(pool.submit(new EstimateCoefficientsExecutor(this, chunk)));
		}

		try {
			// Wait until the work is done
			pool.shutdown();
			if (!pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
				// Error
				System.out.println("Error: Threads not terminated correctly.");
				System.exit(8);
			}
			
			// Get thread results and add them together
			composeResults(futures, LJama, rhsJama);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Matrix parametersJama = solve(LJama, rhsJama);
		determineCoeffs(parametersJama, n);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void composeResults(List<Future> futures, Matrix LJama, Matrix rhsJama) throws InterruptedException, ExecutionException {
		for(Future f : futures) {
			ArrayList<Matrix> buf = (ArrayList<Matrix>)(f.get());
			Matrix b = buf.get(0);
			for(int x=0; x<LJama.getRowDimension(); x++) {
				for(int y=0; y<LJama.getColumnDimension(); y++) {
					setA(LJama, x, y, getA(LJama, x, y) + getA(b, x, y));
				}
			}
			b = buf.get(1);
			for(int x=0; x<rhsJama.getRowDimension(); x++) {
				for(int y=0; y<rhsJama.getColumnDimension(); y++) {
					rhsJama.set(x, y, rhsJama.get(x, y) + b.get(x, y));
				}
			}
		}
	}

	private void determineCoeffs(Matrix parametersJama, int n) {
		coefficients = new SimpleMatrix(dim, n);
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < dim; j++) {
				coefficients.setElementValue(j, i, parametersJama.get(i * dim + j, 0));
			}
		}
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				A.setElementValue(j, i, parametersJama.get(dim * n + i * dim + j, 0));
			}
			b.setElementValue(i, parametersJama.get(dim * n + dim * dim + i, 0));
		}		
	}

	private Matrix solve(Matrix LJama, Matrix rhsJama) {
		Jama.LUDecomposition cd = new Jama.LUDecomposition(LJama);
		return cd.solve(rhsJama);		
	}

	private void setA(Matrix m, int x, int y, double d) {
		m.set(x, y, d);
	}

	private double getA(Matrix m, int x, int y) {
		return m.get(x, y);
	}

	public Object calculateCoefficients(ArrayList<Integer> indices) {
		int n = gridPoints.size();
		int sizeL = dim * n + dim * dim + dim;

		Matrix LJama = new Matrix(sizeL,sizeL);
		Matrix rhsJama = new Matrix(sizeL, 1);
		
		int offset = n*dim;

		for (int i : indices) {
			for (int j = 0; j < values.get(i).getDimension(); j++) {
				rhsJama.set(i*dim+j, 0, values.get(i).getAbstractVector().getElement(j));
			}
			for (int j = i+1; j < n; j++) {
				// symmetric matrix -- compute only upper triangle and write to both locations
				double val = kernel(gridPoints.get(i), gridPoints.get(j)); 
				for (int k = 0; k < dim; k++) {
					int currI = i * dim + k;
					int currJ = j * dim + k;
					LJama.set(currI, currJ, val);
					LJama.set(currJ, currI, val);
				}
			}
			for (int j = 0; j < dim; j++) {
				double val = gridPoints.get(i).get(j);
				for (int k = 0; k < dim; k++) {
					// Symmetric matrix!
					LJama.set(dim * i + k, offset + dim * j + k, val);
					LJama.set(offset + dim * j + k, dim * i + k, val);
				}
			}
			for (int k = 0; k < dim; k++) {
				// Symmetric matrix!
				LJama.set(dim * i + k, offset + dim * dim + k, 1.0);
				LJama.set(offset + dim * dim + k, dim * i + k, 1.0);
			}
		}
		
		ArrayList<Matrix> ret = new ArrayList<Matrix>();
		ret.add(LJama);
		ret.add(rhsJama);
		return ret;
	}
}

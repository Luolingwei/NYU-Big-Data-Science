package Tasks;

import org.ejml.simple.*;

public class SVD {

    @SuppressWarnings("rawtypes")
    public static double[][] reduce (double[][] matrix, int dimensions) {
        SimpleMatrix M = new SimpleMatrix(matrix);
        SimpleSVD s = M.svd();
        SimpleMatrix U = s.getU().extractMatrix(0, SimpleMatrix.END, 0, dimensions);
        SimpleMatrix W = s.getW().extractMatrix(0, dimensions, 0, dimensions);
        SimpleMatrix V = s.getV().extractMatrix(0, dimensions, 0, dimensions);
        SimpleMatrix reducedMatrix = U.mult(W).mult(V.transpose());

        double[][] outMatrix = new double[reducedMatrix.numRows()][reducedMatrix.numCols()];
        for (int i = 0; i < reducedMatrix.numRows(); i++) {
            for (int j = 0; j < reducedMatrix.numCols(); j++) {
                outMatrix[i][j] = reducedMatrix.get(i, j);
            }
        }

        return outMatrix;
    }



}

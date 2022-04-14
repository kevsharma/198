/* 
 * 
 * This code calculates the house price of a house by learing from
 * training data. It uses pseudo inverse of a given matrix to find the 
 * weight of different features.
 * 
 * Predicted Price : Y = W0 + W1*x1 + W2*X2 + W3*X3 + W4*X4
 * Weight Matrix : W = pseudoInv(X)*Y
 * pseudoInv(X) = inverse(transpose(X)*X) * transpose(X)  
 * 
 * weight(w) = pseudoInv(X) * Y
 * 			where	X = Input data matrix
 * 					Y = Target vector
 * 
 */
 
#include<stdio.h>
#include<stdlib.h>

// all methods declarations
double** multiplyMatrix(double **matA, double **matB, int r1, int c1, int r2, int c2);
double** transposeMatrix(double** mat, int row, int col);
double** inverseMatrix(double **matA, int dimension);
void freeMatrix(double** matrix, int row);
void printMatrix(double **matrix, int row, int col);


// main method starts here
int main(int argc, char** argv)
{
    if (argc < 3){
        printf("Error: incorrect arguments to ./ml\n");
        return 0;
    }

	FILE *trainingData = fopen(argv[1], "r");
    FILE *testData = fopen(argv[2], "r");
    if(trainingData == NULL || testData == NULL){
        printf("Error: one or both of the files not found.\n");
        return 0;
    }

    int firstLineTrainingData, secondLineTrainingData; 
    fscanf(trainingData, "%d\n", &firstLineTrainingData);
    fscanf(trainingData, "%d\n", &secondLineTrainingData);
    firstLineTrainingData++; // Need to include the 1s in the first column of X
    
    double** X = malloc(secondLineTrainingData*sizeof(double*));
    double** Y = malloc(secondLineTrainingData*sizeof(double*));
    for(int i=0; i<secondLineTrainingData; i++){
        X[i] = (double *)malloc(firstLineTrainingData*sizeof(double));
        Y[i] = (double *)malloc(sizeof(double));
    }
    
    // Populate X and Y
    for(int i=0; i<secondLineTrainingData; i++){
        X[i][0] = 1;
    }
    for(int i=0; i<secondLineTrainingData; i++){
        for(int j=1; j<firstLineTrainingData; j++){
            fscanf(trainingData, "%lf,", &X[i][j]);
        }
        fscanf(trainingData, "%lf\n", &Y[i][0]);
    }

    fclose(trainingData);

    /** Matrix (Rows, Columns) to ensure argument passes are correct.
     * Note that firstLine was incremeneted to create a proper X array.
     * 
     * Matrix   |    #Rows          |        #Columns
     * ------------------------------------------------
     * X            secondLine               firstLine 
     * XT           firstLine               secondLine
     * XTX          firstLine               firstLine
     * INV          firstLine               firstLine
     * INVXT        firstLine               secondLine
     * Y            secondLine                  1
     * 
     * W = INVXT * Y
     *  
     * W            firstLine                   1            
     */
    double** XT = transposeMatrix(X, secondLineTrainingData, firstLineTrainingData); 
    double** XTX = multiplyMatrix(XT, X, firstLineTrainingData, secondLineTrainingData, secondLineTrainingData, firstLineTrainingData); 
    double** INV = inverseMatrix(XTX, firstLineTrainingData); 
    double** INVXT = multiplyMatrix(INV, XT, firstLineTrainingData, firstLineTrainingData, firstLineTrainingData, secondLineTrainingData); 
    double** W = multiplyMatrix(INVXT, Y, firstLineTrainingData, secondLineTrainingData, secondLineTrainingData, 1);

    freeMatrix(X, secondLineTrainingData);
    freeMatrix(Y, secondLineTrainingData);
    freeMatrix(XT, firstLineTrainingData);
    freeMatrix(XTX, firstLineTrainingData);
    freeMatrix(INV, firstLineTrainingData);
    freeMatrix(INVXT, firstLineTrainingData);

    /*
    *   At this point W contains 
    *   Read the test data and use W to compute the price of each home.
    */    

    int numHouses; 
    fscanf(testData, "%d\n", &numHouses);
    for(int i=0; i<numHouses; ++i){

        double price = W[0][0];
        double xsubj = 0;
        
        for(int j=1; j<firstLineTrainingData-1; j++){
            fscanf(testData, "%lf," , &xsubj);
            price+= (W[j][0] * xsubj);
        }
        fscanf(testData, "%lf", &xsubj);
        price+= (W[firstLineTrainingData-1][0] * xsubj); 
        printf("%0.0f\n", price);
    }

    freeMatrix(W, firstLineTrainingData);
    fclose(testData);
    return 0;
}

double** multiplyMatrix(double **matA, double **matB, int r1, int c1, int r2, int c2)
{
    // result has dimensions r1 * c2 
    double** result=malloc(r1*sizeof(double*)); 
    if(result == NULL){
        printf("Error: could not allocate memory for result\n.");
        return 0;
    }
    for(int i=0; i<r1; i++){
        result[i] = (double *)malloc(c2 * sizeof(double));
        if(result[i] == NULL){
            printf("Error: could not allocate memory for result[%d]\n.", i);
            return 0;
        }
    }
    
    // c1 = r2 (neccessary condition for matrix multiplication)
    // therefore we only need 1 count for those.

    for(int i=0; i<r1; i++){
        for(int j=0; j<c2; j++){
            double sum = 0;
            for(int k=0; k<c1; k++){
                sum += (double)(matA[i][k] * matB[k][j]);
            }
            result[i][j] = (double)sum;
        }
    }

    return result;
}

double** transposeMatrix(double **mat, int row, int col)
{
    // matTran will have dimensions col*row (transpose of row*col)
	double** matTran=malloc(col*sizeof(double*));
    if(matTran == NULL){
        printf("Error: could not allocate memory for matTran\n.");
        return 0;
    }
    // got up to here np
    for(int i=0; i<col; i++){
        matTran[i] = (double*)malloc(row*sizeof(double));
        if(matTran[i] == NULL){
            printf("Error: could not allocate memory for matTran[%d]\n.", i);
            return 0;
        }
        for(int j=0; j<row; ++j)
            matTran[i][j] = mat[j][i];
    }

    return matTran;       
}

double** inverseMatrix(double **matA, int dimension)
{   
    // Note we will create a copy of matA to work with so we do not overwrite it.
    double** matB = malloc(dimension*sizeof(double*));
    if(matB == NULL){
        printf("Error: could not allocate memory for matB\n.");
        return 0;
    }
    for(int i=0; i<dimension; i++){
        matB[i] = (double *)malloc(dimension*sizeof(double));
        if(matB[i] == NULL){
            printf("Error: could not allocate memory for matB\n.");
            return 0;
        }
    }

    for(int i=0; i<dimension; i++){
        for(int j=0; j<dimension; j++){
            matB[i][j] = matA[i][j];
        }
    }

    // Declare matI and initialize it to identity matrix
    double** matI=malloc(dimension*sizeof(double*)); 
    if(matI == NULL){
        printf("Error: could not allocate memory for matI\n.");
        return 0;
    }
    for(int i=0; i<dimension; i++){
        matI[i] = malloc(dimension*sizeof(double*));
        if(matI[i] == NULL){
            printf("Error: could not allocate memory for matI[%d]\n.", i);
            return 0;
        }
    }
    for(int i=0; i<dimension; i++){
        for(int j=0; j<dimension; j++){
            if(i==j){
                matI[i][j] = (double)(1);
            }
            else {
                matI[i][j] = (double)(0);
            }
        }
    }

    // Turn into Upper Triangular
    for(int p=0; p<dimension; ++p){
        double divisor = matB[p][p];

        for(int k=0; k<dimension; k++){
                matB[p][k] = (double)((double)matB[p][k] / (double)divisor); 
                matI[p][k] = (double)((double)matI[p][k] / (double)divisor);
        }
        
        for(int i=p+1; i<dimension; ++i){
            double factor = matB[i][p];
            // Subtract factor*row[i] from row[j] both matB and matI
            for(int k=0; k<dimension; ++k){
                matB[i][k] -= (double)((double)factor * (double)matB[p][k]);
                matI[i][k] -= (double)((double)factor * (double)matI[p][k]);
            }
        }
    }
    // Upper Triangular made, now rest.
    for(int p=dimension-1; p>=0; --p){
        for(int i= p-1; i>=0; --i){
            double factor = matB[i][p];
            for(int k=0; k<dimension; k++){
                matB[i][k] -= (double)((double)factor * (double)matB[p][k]);;
                matI[i][k] -= (double)((double)factor * (double)matI[p][k]);;
            }
        }
    }
    
    freeMatrix(matB, dimension);
	return matI;
}

void freeMatrix(double** matrix, int row){
    
    for(int i=0; i<row; i++){
        free(matrix[i]);
    }

    free(matrix);
    matrix = NULL;
}

void printMatrix(double **matrix, int row, int col){
    printf("{");
    for(int i=0; i<row; i++){
        printf("[");
        for(int j=0; j<col; j++){
            printf(" %lf", matrix[i][j]);
        }
        printf("]\n");
    }
    printf("}\n\n");
}
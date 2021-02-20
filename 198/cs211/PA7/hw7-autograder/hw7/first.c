#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

struct variable {
    char name[16];
    int value;
};
typedef struct variable Variable;

int main(int argc, char* argv[])
{
    if(argc != 2){
        printf("error\n");
        return 0;
    }

    /* Find out how much space to use to store the values */
    FILE* fp = fopen(argv[1], "r");
    char line[500];

    char* firstLine = fgets(line, sizeof(line), fp);
    int numInputVariables;
    sscanf(firstLine, "INPUTVAR %d", &numInputVariables);
    Variable* inputs[numInputVariables];
    for(int i=0; i<numInputVariables; i++){
        inputs[i] = (Variable *) malloc(sizeof(Variable));
    }
    // Tokenize the first line.
    char* token = strtok(firstLine, " ");
    token = strtok(NULL, " ");
    int count = 0;
    while(token != NULL){
        token = strtok(NULL, " ");
        if(token == NULL){
            continue;
        }
        strcpy(inputs[count]->name, token);
        inputs[count]->value = 0;
        ++count;
    }

    char* secondLine = fgets(line, sizeof(line), fp);
    int numOutputVariables;
    sscanf(secondLine, "OUTPUTVAR %d", &numOutputVariables);
    Variable* outputs[numOutputVariables];
    for(int i=0; i<numOutputVariables; i++){
        outputs[i] = (Variable *) malloc(sizeof(Variable));
    }
    // Tokenize the second line
    token = strtok(secondLine, " ");
    token = strtok(NULL, " ");
    count = 0;
    while(token != NULL){
        token = strtok(NULL, " ");
        if(token == NULL){
            continue;
        }
        strcpy(outputs[count]->name, token);
        outputs[count]->value = 0;
        ++count;
    }

    /* Populate instructions based on number of instructions found in the first run of the file */
    int numberOfInstructions = 0;
    while(fgets(line, sizeof(line), fp)){
        ++numberOfInstructions;
    }
    fclose(fp);    

    char* instructions[numberOfInstructions];
    char* x[numberOfInstructions];
    char* y[numberOfInstructions];
    char* o[numberOfInstructions];
    Variable* temps[numberOfInstructions];

    for(int i=0; i<numberOfInstructions; i++){
        instructions[i] = malloc(16 * sizeof(char));
        x[i] = malloc(16 * sizeof(char));
        y[i] = malloc(16 * sizeof(char));
        o[i] = malloc(16 * sizeof(char));
        temps[i] = (Variable *) malloc(16 * sizeof(Variable));
        *temps[i]->name = '\0';
    }

    fp = fopen(argv[1], "r");
    fgets(line, sizeof(line), fp); // get rid of first line.
    fgets(line, sizeof(line), fp); // get rid of second line.

    count = 0; // populate the instructions with proper operands and output.
    while(fgets(line, sizeof(line), fp)){
        if(sscanf(line, "%s %s %s %s", instructions[count], x[count], y[count], o[count]) == 4){
            /* If neither outputs not temps contains the o[count] value, then we must add it to temp */
            int found = 0;
            for(int i=0; i<numOutputVariables; i++){
                if(strncmp(o[count], outputs[i]->name, strlen(o[count])) == 0){
                    found = 1;
                }
            }
            if(found == 0){
                for(int i=0; i<numberOfInstructions; i++){
                    if(*temps[i]->name == '\0'){
                        strcpy(temps[i]->name, o[count]);
                        temps[i]->value = 0;
                        break;
                    }
                    if(strncmp(o[count], temps[i]->name, strlen(o[count])) == 0){
                        break;
                    }
                }
            }            
        }
        else if(sscanf(line, "%s %s %s", instructions[count], x[count], o[count]) == 3){
            *y[count] = '\0';
            /* If neither outputs not temps contains the o[count] value, then we must add it to temp */
            int found = 0;
            for(int i=0; i<numOutputVariables; i++){
                if(strncmp(o[count], outputs[i]->name, strlen(o[count])) == 0){
                    found = 1;
                }
            }
            if(found == 0){
                for(int i=0; i<numberOfInstructions; i++){
                    if(*temps[i]->name == '\0'){
                        strcpy(temps[i]->name, o[count]);
                        temps[i]->value = 0;
                        break;
                    }
                    if(strncmp(o[count], temps[i]->name, strlen(o[count])) == 0){
                        break;
                    }
                }
            }
        }
        ++count;
    }
    fclose(fp);
    /* At this point all of the populating is complete. */
    /*
    printf("\nInputs: ");
    for(int i=0; i<numInputVariables; i++){
        printf("%s ",inputs[i]->name);
    }

    printf("\nOutputs: ");
    for(int i=0; i<numOutputVariables; i++){
        printf("%s ", outputs[i]->name);
    }
    
    printf("\nInstructions X Y and O read as:\n");
    for(int i=0; i<numberOfInstructions; i++){
        printf("in[%s] ", instructions[i]);
        printf("x[%s] ", x[i]);
        printf("y[%s] ", y[i]);
        printf("o[%s]\n", o[i]);
    }

    printf("\nTemps: ");
    for(int i=0; i<numberOfInstructions; i++){
        printf("%s ", temps[i]->name);
    }

    printf("\n\n");    
    */
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /* Now it is time for computations */
    /* We need to compute from 0 to 2^numInputVariables - 1 */
    count = 0;
    while(count < pow(2, numInputVariables)){
        // Set input variables to appropriate binary string as given by count.
        int destructive = count;
        for(int i=numInputVariables-1; i>=0; i--){
            inputs[i]->value = destructive & 1;
            destructive = destructive >> 1;
        }

        // Simulation NOT AND OR NAND NOR XOR
        int operand1 = -1; int operand2 = -1; int operationResult = -1;
        // Find operand1 and operand2
        for(int i=0; i<numberOfInstructions; i++){
            // Search inputs to find operand1 and operand 2.
            for(int op1=0; op1 < numInputVariables; op1++){
                if(strncmp(x[i], inputs[op1]->name, strlen(x[i])) ==0){
                    operand1 = inputs[op1]->value;
                }
                if(strncmp(y[i], inputs[op1]->name, strlen(y[i])) ==0){
                    operand2 = inputs[op1]->value;
                }
            }
            // Search temps to find operand1 and operand2
            for(int op1=0; op1 < numberOfInstructions; op1++){
                if(strlen(temps[op1]->name) == 0){
                    break;
                }

                if(strncmp(x[i], temps[op1]->name, strlen(x[i])) ==0){
                    operand1 = temps[op1]->value;
                }
                if(strncmp(y[i], temps[op1]->name, strlen(y[i])) ==0){
                    operand2 = temps[op1]->value;
                }
            }

            // Compute the operationResult
            if(strcmp(instructions[i], "NOT")==0){
                operationResult = 0;
                if(operand1 == 0){
                    operationResult = 1;
                }
            }
            else if(strcmp(instructions[i], "AND")==0){
                operationResult = 0;
                if(operand1 == 1 && operand2 == 1){
                    operationResult = 1;
                }
            }
            else if(strcmp(instructions[i], "OR")==0){
                operationResult = 0;
                if(operand1 == 1 || operand2 == 1){
                    operationResult = 1;
                }
            }
            else if(strcmp(instructions[i], "NAND")==0){
                operationResult = 1;
                if(operand1 == 1 && operand2 == 1){
                    operationResult = 0;
                }
            }
            else if(strcmp(instructions[i], "NOR")==0){
                operationResult = 1;
                if(operand1 == 1 || operand2 == 1){
                    operationResult = 0;
                }
            }
            else if(strcmp(instructions[i], "XOR")==0){
                operationResult = 1;
                if(operand1 == operand2){
                    operationResult = 0;
                }
            }

            // Set operationResult in its corresponding o[i] either in outputs or temp
            for(int result=0; result < numOutputVariables; result++){
                if(strncmp(o[i], outputs[result]->name, strlen(o[i])) == 0){ // check outputs
                    outputs[result]->value = operationResult;
                }
            }
            for(int result=0; result < numberOfInstructions; result++){ // check temps
                if(strlen(temps[result]->name) == 0){
                    break;
                }
                if(strncmp(o[i], temps[result]->name, strlen(o[i])) == 0){
                    temps[result]->value = operationResult;
                }
            }
        }
        
        // Print the results.
        for(int i=0; i<numInputVariables; i++){
            printf("%d ", inputs[i]->value);
        }
        for(int i=0; i<numOutputVariables; i++){
            printf("%d ", outputs[i]->value);
        }
        printf("\n");

        // Go onto next binary value.
        ++count;
    }
    
    /* Free all memory allocated */
    for(int i=0; i<numberOfInstructions; i++){
        free(instructions[i]);
        free(x[i]);
        free(y[i]);
        free(o[i]);
        free(temps[i]);
    }
    for(int i=0; i<numInputVariables; i++){
        free(inputs[i]);
    }
    for(int i=0; i<numOutputVariables; i++){
        free(outputs[i]);
    } 
    
    return 0;
}
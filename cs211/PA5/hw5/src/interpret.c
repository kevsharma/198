#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// method declarations.
int populateInstructions(char*);
void executeASM(int);

// Global variables and arrays.
int ax = 0, bx = 0, cx = 0, dx = 0;
char* instructions[100];
char* x[100];
char* y[100];
char* L[100];



int main(int argc, char* argv[])
{
    for(int i=0; i<100; i++){
        instructions[i] = malloc(8 * sizeof(char));
        x[i] = malloc(8 * sizeof(char));
        y[i] = malloc(8 * sizeof(char));
        L[i] = malloc(8 *sizeof(char));
    }

	int linesOfCode = populateInstructions(argv[1]);
    executeASM(linesOfCode);

    // Free memory allocated on heap.
    for(int i=0; i<100; i++){
        free(instructions[i]);
        free(x[i]);
        free(y[i]);
        free(L[i]);
    }

    return 0;
}
/*
    for(int i=0; i<linesOfCode; i++){
       printf("Count %d : (instruction: %s) (x: %s) (y: %s) (L: %s)\n", i, instructions[i], x[i], y[i], L[i]);
    }*/
    


/*
* cmd      instructions     x        y     L
* ----------------------------------------------
* mov           mov        #/reg     reg   NULL
* 
* add           add        #/reg     reg   NULL
* sub           sub        #/reg     reg   NULL
* mul           mul        #/reg     reg   NULL
* div           div        #/reg     reg   NULL
*
* je            je         #/reg     #/reg  #
* jne           jne        #/reg     #/reg  #
* jg            jg         #/reg     #/reg  #
* jge           jge        #/reg     #/reg  #
* jl            jl         #/reg     #/reg  #
* jle           jle        #/reg     #/reg  #

* There is a different implementation for print,read,jmp (since they only are two spaces)
* instruction = jmp, x = L
* instruction = print, x = #/reg
* instruction = read, x = reg (destination)

* instruction = "no op"
*/

int populateInstructions(char* filename){

    // Extract from temp and put into instructions,x,y, as appropriate.
    FILE* fp = fopen(filename, "r");

    int count = 0; // # of lines until we hit EOF
    char temp[100];

    while(fgets(temp, sizeof(temp), fp)){
        // temp is empty
        //printf("this is what I read on count %d : %s", count, temp);
        if(strcmp(temp, "\n") == 0){
            //printf("found a noop, now initializing each to appropriate place\n");
            *instructions[count] = '\0'; // no op
            *x[count] = '\0';
            *y[count] = '\0';
            *L[count] = '\0';
        }

        // j* excluding jmp
        else if(sscanf(temp, "%s %s %s %s", instructions[count], L[count], x[count], y[count]) == 4){ 
            // everything initialized.
        }

        // mov / arithmethic
        else if(sscanf(temp, "%s %s %s", instructions[count], x[count], y[count]) == 3){
            *L[count] = '\0';
        }

        // jmp, read, print
        else if(sscanf(temp, "%s %s", instructions[count], x[count]) == 2){
            *y[count] = '\0';
            *L[count] = '\0';
        }

        count++;
    }

    fclose(fp);
    return count;
}

void executeASM(int codeLength){
   int count = 0;
   while(count < codeLength){
       char* cmd = instructions[count];
       
       if(strcmp(cmd, "") == 0){
           ++count;
       }

       else if(strcmp(cmd, "read") == 0){
           char inputNum[10];
           fgets(inputNum, 10, stdin);
           int temp = atoi(inputNum);
           // x holds register.
           if(strcmp(x[count],"ax") == 0){
               ax = temp;
           }
           else if(strcmp(x[count],"bx") == 0){
               bx = temp;
           }
           else if(strcmp(x[count],"cx") == 0){
               cx = temp;
           }
           else if(strcmp(x[count],"dx") == 0){
               dx = temp;
           }

           ++count;
       }

       else if(strcmp(cmd, "print") == 0){
            // instruction = print, x = #/reg
            if(strcmp(x[count], "ax") == 0){
                printf("%d", ax);
            }
            else if(strcmp(x[count], "bx") == 0){
                printf("%d", bx);
            }
            else if(strcmp(x[count], "cx") == 0){
                printf("%d", cx);
            }
            else if(strcmp(x[count], "dx") == 0){
                printf("%d", dx);
            }
            else{
                printf("%d", atoi(x[count]));
            }
            ++count;
       }
       
       else if(strcmp(cmd, "mov") == 0){
           if(strcmp(y[count], "ax") ==0){
               if(strcmp(x[count], "ax") == 0){
                   ax = ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   ax = bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   ax = cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   ax = dx;
               }
               else{
                   ax = atoi(x[count]);
               }
           }
           else if(strcmp(y[count], "bx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   bx = ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   bx = bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   bx = cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   bx = dx;
               }
               else{
                   bx = atoi(x[count]);
               }
           }
           else if(strcmp(y[count], "cx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   cx = ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   cx = bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   cx = cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   cx = dx;
               }
               else{
                   cx = atoi(x[count]);
               }
           }
           else if(strcmp(y[count], "dx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   dx = ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   dx = bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   dx = cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   dx = dx;
               }
               else{
                   dx = atoi(x[count]);
               }
           }

           ++count;
       }

       else if(strcmp(cmd, "add") == 0){
            if(strcmp(y[count], "ax") ==0){
               if(strcmp(x[count], "ax") == 0){
                   ax += ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   ax += bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   ax += cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   ax += dx;
               }
               else{
                   ax += atoi(x[count]);
               }
           }
           else if(strcmp(y[count], "bx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   bx += ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   bx += bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   bx += cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   bx += dx;
               }
               else{
                   bx += atoi(x[count]);
               }
           }
           else if(strcmp(y[count], "cx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   cx += ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   cx += bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   cx += cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   cx += dx;
               }
               else{
                   cx += atoi(x[count]);
               }
           }
           else if(strcmp(y[count], "dx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   dx += ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   dx += bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   dx += cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   dx += dx;
               }
               else{
                   dx += atoi(x[count]);
               }
           }

           ++count;
       }

       else if(strcmp(cmd, "sub") == 0){
            if(strcmp(y[count], "ax") ==0){
               if(strcmp(x[count], "ax") == 0){
                   ax -= ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   ax -= bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   ax -= cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   ax -= dx;
               }
               else{
                   ax -= atoi(x[count]);
               }
           }
           else if(strcmp(y[count], "bx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   bx -= ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   bx -= bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   bx -= cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   bx -= dx;
               }
               else{
                   bx -= atoi(x[count]);
               }
           }
           else if(strcmp(y[count], "cx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   cx -= ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   cx -= bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   cx -= cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   cx -= dx;
               }
               else{
                   cx -= atoi(x[count]);
               }
           }
           else if(strcmp(y[count], "dx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   dx -= ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   dx -= bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   dx -= cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   dx -= dx;
               }
               else{
                   dx -= atoi(x[count]);
               }
           }

           ++count;
       }

       else if(strcmp(cmd, "mul") == 0){
            if(strcmp(y[count], "ax") ==0){
               if(strcmp(x[count], "ax") == 0){
                   ax *= ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   ax *= bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   ax *= cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   ax *= dx;
               }
               else{
                   ax *= atoi(x[count]);
               }
           }
           else if(strcmp(y[count], "bx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   bx *= ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   bx *= bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   bx *= cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   bx *= dx;
               }
               else{
                   bx *= atoi(x[count]);
               }
           }
           else if(strcmp(y[count], "cx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   cx *= ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   cx *= bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   cx *= cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   cx *= dx;
               }
               else{
                   cx *= atoi(x[count]);
               }
           }
           else if(strcmp(y[count], "dx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   dx *= ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   dx *= bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   dx *= cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   dx *= dx;
               }
               else{
                   dx *= atoi(x[count]);
               }
           }

           ++count;
       }

       else if(strcmp(cmd, "div") == 0){
            if(strcmp(y[count], "ax") ==0){
               if(strcmp(x[count], "ax") == 0){
                   ax  = ax/ ax;
               }
               else if(strcmp(x[count], "bx") == 0){
                   ax  = bx/ax;
               }
               else if(strcmp(x[count], "cx") == 0){
                   ax  = cx/ax;
               }
               else if(strcmp(x[count], "dx") == 0){
                   ax  = dx/ax;
               }
               else{
                   ax  = atoi(x[count])/ax;
               }
           }

           else if(strcmp(y[count], "bx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   bx = ax/bx;
               }
               else if(strcmp(x[count], "bx") == 0){
                   bx = bx/bx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   bx = cx/bx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   bx = dx/bx;
               }
               else{
                   bx = atoi(x[count])/bx;
               }
           }
           else if(strcmp(y[count], "cx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   cx = ax/cx;
               }
               else if(strcmp(x[count], "bx") == 0){
                   cx = bx/cx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   cx = cx/cx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   cx = dx/cx;
               }
               else{
                   cx = atoi(x[count])/cx;
               }
           }
           else if(strcmp(y[count], "dx") == 0){
               if(strcmp(x[count], "ax") == 0){
                   dx = ax/dx;
               }
               else if(strcmp(x[count], "bx") == 0){
                   dx= bx/dx;
               }
               else if(strcmp(x[count], "cx") == 0){
                   dx = cx/dx;
               }
               else if(strcmp(x[count], "dx") == 0){
                   dx = dx/dx;
               }
               else{
                   dx = atoi(x[count])/dx;
               }
           }

           ++count;
       }

       else if(strcmp(cmd, "jmp") == 0){
            //instruction = jmp, x = L
            int temp = atoi(x[count]);
            count = temp;
       }

       else if(strcmp(cmd, "je") == 0){
           //je            je         #/reg     #/reg  #
           int processed = 0;
           
           // either could be registers.
           if(strcmp(x[count], "ax") ==0){
               if(strcmp(y[count], "ax") == 0){
                   processed = 1;
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(ax == bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(ax == cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(ax == dx){
                       processed = 1;
                   }
               }
               else{
                   if(ax == atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "bx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(bx == ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   processed = 1;
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(bx == cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(bx == dx){
                       processed = 1;
                   }
               }
               else{
                   if(bx == atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "cx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(cx == ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(cx == bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   processed = 1;
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(cx == dx){
                       processed = 1;
                   }
               }
               else{
                   if(cx == atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "dx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(dx == ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(dx == bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(dx == cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   processed = 1;
               }
               else{
                   if(dx == atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else{
               int num = atoi(x[count]);
               if(strcmp(y[count], "ax") == 0){
                   if(num == ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(num == bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(num == cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(num == dx){
                       processed = 1;
                   }
               }
               else{
                   if(num == atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           if(processed != 0){
                int temp = atoi(L[count]);
                count = temp;
           }
           else
           {
               count++;
           }
           
       }

       else if(strcmp(cmd, "jne") == 0){

           int processed = 0;
           
           // either could be registers.
           if(strcmp(x[count], "ax") ==0){
               if(strcmp(y[count], "ax") == 0){
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(ax != bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(ax != cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(ax != dx){
                       processed = 1;
                   }
               }
               else{
                   if(ax != atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "bx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(bx != ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(bx != cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(bx != dx){
                       processed = 1;
                   }
               }
               else{
                   if(bx != atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "cx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(cx != ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(cx != bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(cx != dx){
                       processed = 1;
                   }
               }
               else{
                   if(cx != atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "dx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(dx != ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(dx != bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(dx != cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
               }
               else{
                   if(dx != atoi(y[count])){
                       processed = 1;
                   }
               }
           }
           
           else{
               int num = atoi(x[count]);
               if(strcmp(y[count], "ax") == 0){
                   if(num != ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(num != bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(num != cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(num != dx){
                       processed = 1;
                   }
               }
               else{
                   if(num != atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           if(processed != 0){
                int temp = atoi(L[count]);
                count = temp;
           }
           else
           {
               count++;
           }
       }

       else if(strcmp(cmd, "jg") == 0){
           //je            je         #/reg     #/reg  #
           int processed = 0;
           
           // either could be registers.
           if(strcmp(x[count], "ax") ==0){
               if(strcmp(y[count], "ax") == 0){
               }

               else if(strcmp(y[count], "bx") == 0){
                   if(ax > bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(ax > cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(ax > dx){
                       processed = 1;
                   }
               }
               else{
                   if(ax > atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "bx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(bx > ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(bx > cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(bx > dx){
                       processed = 1;
                   }
               }
               else{
                   if(bx > atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "cx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(cx > ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(cx > bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(cx > dx){
                       processed = 1;
                   }
               }
               else{
                   if(cx > atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "dx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(dx > ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(dx > bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(dx > cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
               }
               else{
                   if(dx > atoi(y[count])){
                       processed = 1;
                   }
               }
           }
           
           else{
               int num = atoi(x[count]);
               if(strcmp(y[count], "ax") == 0){
                   if(num > ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(num > bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(num > cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(num > dx){
                       processed = 1;
                   }
               }
               else{
                   if(num > atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           if(processed != 0){
                int temp = atoi(L[count]);
                count = temp;
           }
           else
           {
               count++;
           }
       }

       else if(strcmp(cmd, "jge") == 0){
           int processed = 0;
           
           // either could be registers.
           if(strcmp(x[count], "ax") ==0){
               if(strcmp(y[count], "ax") == 0){
                   processed = 1;
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(ax >= bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(ax >= cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(ax >= dx){
                       processed = 1;
                   }
               }
               else{
                   if(ax >= atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "bx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(bx >= ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   processed = 1;
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(bx >= cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(bx >= dx){
                       processed = 1;
                   }
               }
               else{
                   if(bx >= atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "cx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(cx >= ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(cx >= bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   processed = 1;
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(cx >= dx){
                       processed = 1;
                   }
               }
               else{
                   if(cx >= atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "dx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(dx >= ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(dx >= bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(dx >= cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   processed = 1;
               }
               else{
                   if(dx >= atoi(y[count])){
                       processed = 1;
                   }
               }
           }
           
           else{
               int num = atoi(x[count]);
               if(strcmp(y[count], "ax") == 0){
                   if(num >= ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(num >= bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(num >= cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(num >= dx){
                       processed = 1;
                   }
               }
               else{
                   if(num >= atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           if(processed != 0){
                int temp = atoi(L[count]);
                count = temp;
           }
           else
           {
               count++;
           }
       }

       else if(strcmp(cmd, "jl") == 0){
           int processed = 0;
           
           // either could be registers.
           if(strcmp(x[count], "ax") ==0){
               if(strcmp(y[count], "ax") == 0){
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(ax < bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(ax < cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(ax < dx){
                       processed = 1;
                   }
               }
               else{
                   if(ax < atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "bx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(bx < ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(bx < cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(bx < dx){
                       processed = 1;
                   }
               }
               else{
                   if(bx < atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "cx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(cx < ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(cx < bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(cx < dx){
                       processed = 1;
                   }
               }
               else{
                   if(cx < atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "dx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(dx < ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(dx < bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(dx < cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
               }
               else{
                   if(dx < atoi(y[count])){
                       processed = 1;
                   }
               }
           }
           
           else{
               int num = atoi(x[count]);
               if(strcmp(y[count], "ax") == 0){
                   if(num < ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(num < bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(num < cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(num < dx){
                       processed = 1;
                   }
               }
               else{
                   if(num < atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           if(processed != 0){
                int temp = atoi(L[count]);
                count = temp;
           }
           else
           {
               count++;
           }
       }

       else if(strcmp(cmd, "jle") == 0){
           int processed = 0;
           
           // either could be registers.
           if(strcmp(x[count], "ax") ==0){
               if(strcmp(y[count], "ax") == 0){
                   processed = 1;
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(ax <= bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(ax <= cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(ax <= dx){
                       processed = 1;
                   }
               }
               else{
                   if(ax <= atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "bx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(bx <= ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   processed = 1;
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(bx <= cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(bx <= dx){
                       processed = 1;
                   }
               }
               else{
                   if(bx <= atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "cx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(cx <= ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(cx <= bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   processed = 1;
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(cx <= dx){
                       processed = 1;
                   }
               }
               else{
                   if(cx <= atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           else if(strcmp(x[count], "dx") ==0){
               if(strcmp(y[count], "ax") == 0){
                   if(dx <= ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(dx <= bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(dx <= cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   processed = 1;
               }
               else{
                   if(dx <= atoi(y[count])){
                       processed = 1;
                   }
               }
           }
           
           else{
               int num = atoi(x[count]);
               if(strcmp(y[count], "ax") == 0){
                   if(num <= ax){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "bx") == 0){
                   if(num <= bx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "cx") == 0){
                   if(num <= cx){
                       processed = 1;
                   }
               }
               else if(strcmp(y[count], "dx") == 0){
                   if(num <= dx){
                       processed = 1;
                   }
               }
               else{
                   if(num <= atoi(y[count])){
                       processed = 1;
                   }
               }
           }

           if(processed != 0){
                int temp = atoi(L[count]);
                count = temp;
           }
           else
           {
               count++;
           }
       }
   } 
}
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <omp.h>
#include <sys/time.h>

// https://connect.ed-diamond.com/GNU-Linux-Magazine/glmf-122/decouverte-de-la-programmation-parallele-avec-openmp

#define NB_THREADS omp_get_num_procs()
#define DIMENSION 1000
#define DEBUG false

long getCurrentTimeInMs()
{
    struct timeval time;
    gettimeofday(&time, NULL);
    return time.tv_sec * 1000LL + time.tv_usec / 1000;
}

void printMatrice(int **matrice)
{
    if (!DEBUG)
    {
        return;
    }
    for (int lineIdx = 0; lineIdx < DIMENSION; lineIdx++)
    {
        for (int colIdx = 0; colIdx < DIMENSION; colIdx++)
        {
            printf("%d ", matrice[lineIdx][colIdx]);
        }
        printf("\n");
    }
    printf("\n");
}

int **produitMatricielMonothread(int **matrice1, int **matrice2)
{
    int **result;
    result = malloc(sizeof(int *) * DIMENSION);

    for (int lineIdx = 0; lineIdx < DIMENSION; lineIdx++)
    {
        result[lineIdx] = malloc(sizeof(int) * DIMENSION);
        for (int colIdx = 0; colIdx < DIMENSION; colIdx++)
        {
            result[lineIdx][colIdx] = 0;
            for (int idx = 0; idx < DIMENSION; idx++)
            {
                result[lineIdx][colIdx] += matrice1[lineIdx][idx] * matrice2[idx][colIdx];
            }
        }
    }

    return result;
}

int **produitMatricielMultithread(int **matrice1, int **matrice2)
{
    int **result;
    result = malloc(sizeof(int *) * DIMENSION);

    for (int lineIdx = 0; lineIdx < DIMENSION; lineIdx++)
    {
        result[lineIdx] = malloc(sizeof(int) * DIMENSION);
        // pareil que #pragma omp parallel for num_threads(NB_THREADS) private(colIdx,idx)
#pragma omp parallel for
        for (int colIdx = 0; colIdx < DIMENSION; colIdx++)
        {
            result[lineIdx][colIdx] = 0;
            for (int idx = 0; idx < DIMENSION; idx++)
            {
                if (DEBUG)
                {
                    printf("Element %d / %d / %d traité par le thread %d \n", lineIdx, colIdx, idx, omp_get_thread_num());
                }
                result[lineIdx][colIdx] += matrice1[lineIdx][idx] * matrice2[idx][colIdx];
            }
        }
    }

    return result;
}

int **generateRandomMatrice()
{
    int **result;
    // possible de réaliser la matrice en une seule dimension et de jouer avec les indices pour simuler des lignes
    result = malloc(sizeof(int *) * DIMENSION);
    for (int lineIdx = 0; lineIdx < DIMENSION; lineIdx++)
    {
        result[lineIdx] = malloc(sizeof(int) * DIMENSION);
        for (int colIdx = 0; colIdx < DIMENSION; colIdx++)
        {
            result[lineIdx][colIdx] = rand() % 10;
        }
    }

    return result;
}

void freeMatrice(int **matrice)
{
    for (int lineIdx = 0; lineIdx < DIMENSION; lineIdx++)
    {
        free(matrice[lineIdx]);
    }

    free(matrice);
}

int main(int argc, char const *argv[])
{
    omp_set_num_threads(NB_THREADS);
    int **matrice1;
    int **matrice2;

    matrice1 = generateRandomMatrice();
    matrice2 = generateRandomMatrice();

    printMatrice(matrice1);
    printMatrice(matrice2);

    double start = getCurrentTimeInMs();
    printMatrice(produitMatricielMonothread(matrice1, matrice2));
    printf("Produit matriciel monothread : %ld ms\n\n", (long)(getCurrentTimeInMs() - start));

    start = getCurrentTimeInMs();
    printMatrice(produitMatricielMultithread(matrice1, matrice2));
    printf("Produit matriciel multithread : %ld ms\n\n", (long)(getCurrentTimeInMs() - start));

    freeMatrice(matrice1);
    freeMatrice(matrice2);

    return EXIT_SUCCESS;
}
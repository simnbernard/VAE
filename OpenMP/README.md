# OpenMP

Guide : https://connect.ed-diamond.com/GNU-Linux-Magazine/glmf-122/decouverte-de-la-programmation-parallele-avec-openmp

## Configuration macOS

```bash
brew install llvm libomp
brew reinstall gcc
xcode-select --install
```

## Compilatation & exécution

```bash
gcc-13 -Wall -fopenmp -o ProgrammeOpenMP ProgrammeOpenMP.c && ./ProgrammeOpenMP
```

@echo off
echo Borrando archivos .sol de manera recursiva en el directorio %cd%...

:: Borra archivos .sol de manera recursiva en el directorio actual y sus subdirectorios
del /S /Q *.sol

echo Borrado completado.
pause
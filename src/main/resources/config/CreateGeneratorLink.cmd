del ..\..\..\..\target\config\generator.yml
del ..\..\..\..\target\config\generator-template.yml
IF NOT EXIST "..\..\..\..\target\config" MD "..\..\..\..\target\config"
mklink /h ..\..\..\..\target\config\generator.yml generator.yml
mklink /h ..\..\..\..\target\config\generator-template.yml generator-template.yml
pause
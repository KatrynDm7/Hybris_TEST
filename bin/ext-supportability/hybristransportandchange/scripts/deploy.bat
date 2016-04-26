@ECHO OFF
REM============================================================================
REM  Script to run the deployment process.                                    =
REM                                                                           =
REM  SYNTAX:                                                                  =
REM  deploy.bat mode appl-type connection user password [file]                =
REM                                                                           =
REM    where:                                                                 =
REM     - mode -> CONNECT or DEPLOY                                           =
REM        CONNECT mode tests if the deployment is possible                   =
REM        DEPLOY mode performs deployment of the specified file to the       =
REM               specified destination                                       =
REM                                                                           =
REM     - appl-type ->application type of deployment the script has to process=
REM                                                                           =
REM     - connection -> a connection data specifying the target destination of=
REM     deployment; can contain spaces inside i.e. "host_name 8888 c://deploy"=
REM                                                                           =
REM     - user -> user name (credentials to deploy the file)                  =
REM                                                                           =
REM     - password -> user's password (old) or "filled" (new)                 =
REM                                                                           =
REM     - file -> the absolute path of a file to be deployed, mandatory       =
REM               only for DEPLOY mode                                        =
REM                                                                           =
REM                                                                           =
REM  The script should return a response code defined as:                     =
REM     RC = 0  -> the script successfully completed                          =
REM     RC = 8  -> some content related error happened during transport       =
REM                entity deployment;                                         =
REM     RC = 12 -> some non-content related error happened during transport   =
REM                entity deployment. The next entities should be processed;  =  
REM     RC = 13 -> some non-content related error happened during transport   =
REM                entity deployment. The whole deployment process should be  =
REM                stopped.                                                   =  
REM                                                                           =
REM  In the case of any errors, the proper error description should be printed= 
REM  into the command prompt.                                                 =
REM                                                                           =
REM  EXAMPLES:                                                                =
REM     deploy.bat CONNECT DEMO localhost user password                       =
REM     deploy.bat DEPLOY DEMO c:\deploy user pass c:\files\test.jar          =
REM     deploy.bat DEPLOY DEMO "10.10.10.255 c:\deploy" user pass c:\test.jar = 
REM============================================================================
PUSHD "%~dp0"
IF "%~1"=="/?" GOTO help
IF "%~1"=="help" GOTO help
IF "%~1"=="" GOTO instruct
IF "%~2"=="" GOTO instruct
IF "%~3"=="" GOTO instruct
IF "%~4"=="" GOTO instruct
IF "%~5"=="" GOTO instruct

ECHO ----------------
ECHO Script Starts...
ECHO ----------------
 
IF "%~2"=="HYBRIS" GOTO :hybris
GOTO bad_type

:help
ECHO.
ECHO  DEPLOY.BAT 
ECHO.
ECHO  Script to run the deployment process. 
ECHO. 
ECHO  SYNTAX:
ECHO.
ECHO  deploy.bat mode appl-type connection user password [file]
ECHO.
ECHO    where:
ECHO     - mode : CONNECT or DEPLOY
ECHO        CONNECT mode tests if the deployment is possible
ECHO        DEPLOY mode performs deployment of the specified file to the
ECHO           specified destination
ECHO.
ECHO     - appl-type : application type of deployment the script has to process
ECHO.
ECHO     - connection : a connection data specifying the target destination of
ECHO     deployment; can contain spaces inside i.e. "host_name 8888 c://deploy"
ECHO.
ECHO     - user : user name (credentials to deploy the file)
ECHO.
ECHO     - password : user's password (old) or "filled" (new)
ECHO.
ECHO     - file : the absolute path of a file to be deployed,
ECHO              only mandatory for the DEPLOY mode
ECHO.
ECHO.
ECHO  The script should return a response code defined as:
ECHO.
ECHO     RC = 0  : the script successfully completed
ECHO     RC = 8  : some content related error happened during transport entity 
ECHO               deployment
ECHO     RC = 12 : some non-content related error happened during transport 
ECHO               entity deployment. The next entities should be processed
ECHO     RC = 13 : some non-content related error happened during transport
ECHO               entity deployment. The whole deployment process should be
ECHO               stopped
ECHO.
ECHO  In the case of any errors, the proper error description should be printed 
ECHO  into the command prompt.
ECHO.
ECHO  EXAMPLES:
ECHO.
ECHO     deploy.bat CONNECT DEMO localhost user password
ECHO     deploy.bat DEPLOY DEMO c:\deploy user pass c:\files\test.jar
ECHO     deploy.bat DEPLOY DEMO "10.10.10.255 c:\deploy" user pass c:\\test.jar
GOTO :EOF

:bad_type
ECHO RC = 12
ECHO  The application type [%2] can not be processed by this script. >&2
exit 12

:hybris
set params=%*
call hybris/deploy_HYBRIS.bat %params%
exit ERRORLEVEL

:end
ECHO Script Done.
REM exit 12
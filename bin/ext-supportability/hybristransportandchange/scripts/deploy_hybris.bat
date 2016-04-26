@ECHO OFF
REM============================================================================
REM  Script to run the deployment process for HYBRIS application type.          =
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
REM     - password -> user's password                                         =
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

ECHO -------------------------
ECHO HYBRIS Script Starts...
ECHO -------------------------

REM=====================================================================================
REM  SCRIPT CONFIGURATION                                                              =
REM                                                                                    =
REM  SET YOUR JAVA ENVIRONMENT:                                                        =
REM    You must specify the Java home directory here                                    =
REM                                                                                    =
REM=====================================================================================

SET JAVA_HYBRIS=PUT_YOUR_PATH_TO_JAVA_HERE

SET JAVA="%JAVA_HYBRIS%"\bin\java.exe

set mode="%~1"
set appltype="%~2"
set connection="%~3"
set user="%~4"
set password="%~5"
set file="%~6"
if %password%=="filled" (
  echo reading password
  set /P password=
)

set fileName=%~n6

IF /I %mode%=="CONNECT" GOTO connect
IF /I %mode%=="DEPLOY" GOTO deploy
GOTO notdef


:deploy
REM process only requests for the application type HYBRIS
IF NOT %appltype%=="HYBRIS" GOTO bad_type
ECHO APPLICATION TYPE: %appltype%
ECHO CONNECTION: %connection%

FOR /f "tokens=* delims=;" %%a IN (%connection:'=%) DO call :deployment %%a

:deployment
SET TARGET_DIR=\\%1\%3
ECHO Target_dir is: %TARGET_DIR%

NET use N: %TARGET_DIR% /USER:%user% %password%

REM check first if the specified destination (target) exists
REM ECHO NET USE RC: %ERRORLEVEL% 

IF NOT EXIST %TARGET_DIR% GOTO bad_target
IF NOT EXIST %file% GOTO error 

ECHO Copying files
COPY %file% %TARGET_DIR%
NET USE N: /DELETE

%JAVA% -jar -Drmi_url=%1 -Drmi_port=%4 rmiCtsClient.jar "%fileName%.zip" "%2\%3"

echo errorlevel:  %errorlevel%
ECHO  RC = %errorlevel%
ECHO Script Done.
exit %errorlevel%
REM End of logic to unzip files and execute recipe
REM ***************************************************************************************************************

:connect
REM process only requests for the application type HYBRIS
IF NOT %appltype%=="HYBRIS" GOTO bad_type
REM Here is a simple logic to check if the specified destination (folder) exists.
ECHO - Test Connection Mode
ECHO - Deploy URI: [%connection%]

FOR /f "tokens=* delims=;" %%a IN (%connection:'=%) DO call :connect_test %%a

:connect_test
SET TARGET_DIR=\\%1\%3
ECHO - Target_dir: [%TARGET_DIR%]

NET use N: %TARGET_DIR% /USER:%user% %password%

REM check first if the specified destination (target) exists
REM ECHO NET USE RC: %ERRORLEVEL% 

IF NOT EXIST %TARGET_DIR% GOTO bad_target

IF EXIST %TARGET_DIR% GOTO is_writeable
NET USE N: /DELETE
ECHO Target Dir: [%TARGET_DIR] does not exist >&2
ECHO RC = 12
GOTO end

:is_writeable
COPY deploy_hybris.bat N:\
IF EXIST N:\deploy_hybris.bat GOTO write_ok
NET USE N: /DELETE
ECHO RC = 12
ECHO Target %TARGET_DIR% is not writable >&2
GOTO end 

:write_ok
DEL N:\deploy_hybris.bat
NET USE N: /DELETE
echo Target %TARGET_DIR% is writable
ECHO RC = 0
exit 0

:notdef
ECHO  RC = 12
ECHO  The script mode is not valid. Valid modes are: CONNECT, DEPLOY >&2
GOTO end

:instruct
ECHO  RC = 12
ECHO.
ECHO  The script requires at least 5 parameters. >&2
ECHO  Please, read the help instructions.
GOTO end

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
ECHO     - password : user's password
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
ECHO  The application type [%appltype%] can not be processed by this script. >&2
GOTO end

:error
ECHO  RC = 8
ECHO  File %file% does not exist or cannot be read. >&2
exit 8

:bad_target
ECHO  RC = 12
ECHO  The target [%connection%] either does not exist or is not writable >&2
GOTO end

:end
ECHO Script Done.
exit 12
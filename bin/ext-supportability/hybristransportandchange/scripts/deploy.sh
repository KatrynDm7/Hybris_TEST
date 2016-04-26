#!/bin/sh
cd $(dirname "${0}")
############################################################################
# Script to run the deploy process.  
#                                                                          #
# USAGE:                                                                   #
# deploy.sh mode appl-type connection user password [file]                 #
#                                                                          #
#   where:                                                                 #
#    - mode -> CONNECT or DEPLOY                                           #
#       CONNECT mode tests if the deployment is possible                   #
#       DEPLOY mode performs deployment of the specified file to the       #
#              specified destination                                       #
#                                                                          #
#    - appl-type ->application type of deployment the script has to process#
#                                                                          #
#    - connection -> a connection data specifying the target destination of#
#    deployment; can contain spaces inside i.e. "host_name 8888 /deploy"   #
#                                                                          #
#    - user -> user name (credentials to deploy the file)                  #
#                                                                          #
#    - password -> user's password                                         #
#                                                                          #
#    - file -> the absolute path of a file to be deployed, mandatory       #
#              only for DEPLOY mode                                        #
#                                                                          #
#    - appl-type -> application type of the deployment                     #
#                                                                          #
# The script should return the response code defined as:                   #
#    RC = 0  -> the script successfully completed                          #
#    RC = 8  -> some content related error happened during transport       #
#               entity deployment;                                         #
#    RC = 12 -> some non-content related error happened during transport   #
#               entity deployment. The next entities should be processed;  #  
#    RC = 13 -> some non-content related error happened during transport   #
#               entity deployment. The whole deployment process should be  #
#               stopped.                                                   #  
#                                                                          #
# In the case of any errors, the proper error description should be printed# 
# into the command prompt.                                                 #
#                                                                          #
# EXAMPLES:                                                                #
#    deploy.sh CONNECT DEMO localhost user password                        # 
#    deploy.sh DEPLOY DEMO c:/deploy user pass c:/files/test.jar           #
#    deploy.sh DEPLOY DEMO "10.10.10.255 c:/deploy" user pass c:/test.jar  # 
#                                                                          #
############################################################################
echo "Script name is     [$0]"
#echo "All Parameters     [$@]"
echo "No. of Parameters  [$#]"

if [ "$1" = "help" ] || [ "$1" = "HELP" ]
then
echo 
echo " deploy.sh"
echo 
echo " Script to run the deploy process."
echo 
echo " USAGE:"
echo 
echo " deploy.sh mode appl-type connection user password [file]"
echo 
echo "  where:"
echo "   - mode : CONNECT or DEPLOY"
echo "      CONNECT mode tests if the deployment is possible"
echo "      DEPLOY mode performs deployment of the specified file to the"
echo "             specified destination"
echo 
echo "   - appl-type : application type of deployment the script has to process"
echo
echo "   - connection : a connection data specifying the target destination of"
echo "     deployment; can contain spaces inside i.e. [host_name 8888 /deploy]"
echo 
echo "   - user : user name (credentials to deploy the file)"
echo 
echo "   - password : user's password"
echo 
echo "   - file : the absolute path of a file to be deployed,"
echo "            only mandatory for the DEPLOY mode"
echo
echo " The script should return the response code defined as:"
echo
echo "     RC = 0  : the script successfully completed"
echo "     RC = 8  : some content related error happened during transport"
echo "               entity deployment;"
echo "     RC = 12 : some non-content related error happened during transport"
echo "               entity deployment. The next entities should be processed;"
echo "     RC = 13 : some non-content related error happened during transport"
echo "               entity deployment. The whole deployment process should be"
echo "               stopped."
echo
echo " In the case of any errors, the proper error description should be printed"
echo " into the command prompt."
echo
echo " EXAMPLES:"
echo
echo "  deploy.sh CONNECT DEMO localhost user password"
echo "  deploy.sh DEPLOY DEMO /deploy user pass /files/test.jar"
echo "  deploy.sh DEPLOY DEMO "10.10.10.255 /deploy" user pass /files/test.jar"
exit 0
fi

echo "----------------"
echo "Script Starts..."
echo "----------------"

if [ $# -lt 5 ]
then
   echo  "RC = 12"
   echo  "The script requires at least 5 parameters." 1>&2
   echo  "Please, read the help instructions."
   exit 12
fi

if [ "$2" = "HYBRIS" ] 
then
	cd hybris
    echo "Application Type $2"
	./deploy_hybris.sh "$1" "$2" "$3" "$4" "$5" "$6"	
	if [ $? -ne 0 ]
		then
		echo "RC = 8"							
		exit 8 
	fi	
else
	echo "Unsupported application type $2" 1>&2
    exit 12
fi


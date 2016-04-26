#!/bin/sh
############################################################################
# Script to run the deploy process for DEMO application type.              #
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
#    deploy.sh CONNECT HYBRIS localhost user password                      #
#    deploy.sh DEPLOY HYBRIS "user@host:/root/deploy/ 1098" user pass /root/Documents/test.zip  # 
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
echo "  deploy.sh CONNECT DEMO "user@host:directory" user password"
echo "  deploy.sh DEPLOY DEMO "user@host:directory" user pass /files/test.jar"
exit 0
fi

echo "-------------------------"
echo "HYBRIS Deployment Starts..."
echo "-------------------------"

#=====================================================================================
#  SCRIPT CONFIGURATION                                                              =
#                                                                                    =
#  SET YOUR JAVA ENVIRONMENT:                                                        =
#    You must specify the Java home directory,                                       =
#    either by setting a SAPCC_JAVA_HOME environment variable or by uncommenting and =
#    replacing <JAVA_PATH> in the following line.                                    =
#                                                                                    =
#=====================================================================================
JAVA_HYBRIS=PUT_YOUR_PATH_TO_JAVA_HERE
JAVA=$JAVA_HYBRIS/bin/java

mode="$1"
appltype="$2"
connection="$3"

paramsArray=($connection)
connection=${paramsArray[0]}
rmiPort=${paramsArray[1]}

user="$4"
password="$5"
file="$6"

targetServer=${connection%:*}
targetFolder=${connection##*:}
currentFolder=$(pwd)

if [ "$password" = "filled" ]
then
  echo "reading password"
  read password
  echo "password received"
fi

if [ $# -lt 5 ]
then
   echo  "RC = 12"
   echo  "The script requires at least 5 parameters." 1>&2
   echo  "Please, read the help instructions."
   exit 12
fi

if [ "$mode" = "CONNECT" ] || [ "$mode" = "connect" ]
then
    echo "Test connection mode"
    if [ "$appltype" = "HYBRIS" ] || [ "$appltype" = "hybris" ]
    then       
		echo "Target Dir: [$connection]"
		
		echo "#!/bin/bash" > $currentFolder/dynamicScriptToTestC.sh
		echo "if [ -d "$targetFolder" ] && [ -w "$targetFolder" ]" >> $currentFolder/dynamicScriptToTestC.sh
		echo "then" >> $currentFolder/dynamicScriptToTestC.sh
		   echo "echo 'Target $connection exists and is writable'" >> $currentFolder/dynamicScriptToTestC.sh
		   echo "echo 'RC = 0'" >> $currentFolder/dynamicScriptToTestC.sh
		   echo "exit 0" >> $currentFolder/dynamicScriptToTestC.sh
		echo "else" >> $currentFolder/dynamicScriptToTestC.sh
		   echo "echo RC = 12" >> $currentFolder/dynamicScriptToTestC.sh
		   echo "echo 'Target $connection either does not exist or is not writable' 1>&2" >> $currentFolder/dynamicScriptToTestC.sh
		   echo "exit 12" >> $currentFolder/dynamicScriptToTestC.sh
		echo "fi" >> $currentFolder/dynamicScriptToTestC.sh		
		
		chmod +x $currentFolder/dynamicScriptToTestC.sh
		scp $currentFolder/dynamicScriptToTestC.sh "$connection"

		ssh "$targetServer" $targetFolder/dynamicScriptToTestC.sh
		ssh "$targetServer" "rm $targetFolder/dynamicScriptToTestC.sh"

		rm $currentFolder/dynamicScriptToTestC.sh

    else
       echo RC = 12
       echo "The application type [$appltype] can not be processed by this script." 1>&2
	   exit 12
    fi
elif [ "$mode" = "DEPLOY" ] || [ "$mode" = "deploy" ]
	then
		echo "Deploy mode"
		echo "Note, it processes only requests of the application type HYBRIS"
		if [ "$appltype" = "HYBRIS" ] || [ "$appltype" = "hybris" ]
			then
			echo "Application Type: $appltype"
			
			if [ -e "$file" ]
				then
				
				if [ -r "$file" ]
					then 		   						
						fileName=${file##*/}
						host=${targetServer##*@}						
												
						scp "$file" "$connection"
						if [ $? -ne 0 ]
							then
							echo "RC = 12"
							exit 12
						fi
						
						$JAVA -jar -Drmi_url=$host -Drmi_port=$rmiPort rmiCtsClient.jar $fileName $targetFolder
						
						if [ $? -ne 0 ] 
							then
							echo "RC = 8"							
							exit 8
						fi						
						
					echo "RC = 0"
					echo everything was fine
					exit 0
				else
				  echo "RC = 8"
				  echo "File $file is not readable." 1>&2
				  exit 8
				fi
			else
				echo "RC = 8"
				echo "File $file does not exist or is directory." 1>&2
				exit 8							
			fi
		else
			echo "The application type $appltype is invalid" 1>&2
			echo "RC = 12"
			exit 12
		fi
fi
	
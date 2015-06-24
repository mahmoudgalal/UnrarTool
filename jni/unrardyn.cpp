

#include "com_aroma_unrartool_Unrar.h"
#include <string.h>
#include <android/log.h>
#include "rar.hpp"
#include "version.hpp"
#include "dll.hpp"


#define  LOG_TAG    "libunrar-jni"
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define MAX_COMMENT_SIZE 64*1024

jmethodID getPassWordID;
jfieldID archiveCmtfid;
jfieldID archivelocked,archivesigned ,archivefirstVolume
     ,archiverecoveryRecord,archivesolid,archivecommentPresent,archivevolume;
struct Environment
{
	JNIEnv* env;
	jobject obj;
}environment;

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
        JNIEnv* env;
        if (vm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) return -1;

        return JNI_VERSION_1_6;
}

void JNI_OnUnload(JavaVM *, void *)
{
}
JNIEXPORT void JNICALL Java_com_aroma_unrartool_Unrar_init
  (JNIEnv * env, jclass cls)
{
  getPassWordID = env->GetMethodID(cls, "getPassWord", "()Ljava/lang/String;");
  if(!getPassWordID)
	  LOGE("Error:couldn't get methodid of method: %s","getPassWord");
  archiveCmtfid = env->GetFieldID(cls, "archiveComment","Ljava/lang/String;");
  if (archiveCmtfid == NULL) {
		LOGE("Error:couldn't get FieldID of Field: %s","archiveComment");
	}
  archivelocked = env->GetFieldID(cls, "locked","Z");
  if (archivelocked == NULL) {
		LOGE("Error:couldn't get FieldID of Field: %s","archivelocked");
	}
  archivesigned = env->GetFieldID(cls, "signed","Z");
  if (archivesigned == NULL) {
		LOGE("Error:couldn't get FieldID of Field: %s","archivesigned");
	}
  archiverecoveryRecord = env->GetFieldID(cls, "recoveryRecord","Z");
  if (archiverecoveryRecord == NULL) {
		LOGE("Error:couldn't get FieldID of Field: %s","archiverecoveryRecord");
	}
  archivesolid = env->GetFieldID(cls, "solid","Z");
  if (archivesolid == NULL) {
		LOGE("Error:couldn't get FieldID of Field: %s","archivesolid");
	}
  archivecommentPresent = env->GetFieldID(cls, "commentPresent","Z");
  if (archivecommentPresent == NULL) {
		LOGE("Error:couldn't get FieldID of Field: %s","archivecommentPresent");
	}
  archivevolume = env->GetFieldID(cls, "volume","Z");
  if (archivevolume == NULL) {
		LOGE("Error:couldn't get FieldID of Field: %s","archivevolume");
	}
  archivefirstVolume = env->GetFieldID(cls, "firstVolume","Z");
  if (archivefirstVolume == NULL) {
		LOGE("Error:couldn't get FieldID of Field: %s","archivefirstVolume");
	}
}

int UnRarCallBack(UINT msg,LPARAM UserData,LPARAM P1,LPARAM P2)
{
	//LOGI("UnRarCallBack called with msg:%d \n",msg);
	int ret=0;
	switch(msg)
	{
		case UCM_CHANGEVOLUME    :
			if(P2==RAR_VOL_ASK)
			{
				LOGI("Required volume %s \n",(char*)P1);
				ret=1;
			}
			else if(P2==RAR_VOL_NOTIFY)
			{
				LOGI("Required volume %s is successfully opened \n",(char*)P1);
				ret = 1;
			}			
			break;
		case UCM_PROCESSDATA     :
			ret=1;
			break;
		case UCM_NEEDPASSWORDW    :			
				{
				   LOGI("archive - UCM_NEEDPASSWORDW - needs password \n");
				   ret=-1;
				   if(false)
				if(getPassWordID )
				{
					Environment* enviro=(Environment*)UserData;
					jstring jstr = (jstring) enviro->env->CallObjectMethod(enviro->obj, getPassWordID);
					if(jstr)
					{
                        char outbuf[128];
						//pass[0]='m';pass[1]='a';pass[2]='h';pass[3]='m';pass[4]='1';pass[5]='9';pass[6]='8';pass[7]='5';
						//char* outbuf="mahm1985";
						int len = enviro->env->GetStringLength( jstr);
						enviro->env->GetStringUTFRegion( jstr, 0, len, outbuf);
						LOGI("archive given password is %s with length %d\n",outbuf,len);
						char * temp=(char*)P1;
						for(int i=0,j=0;i<2*len,j<len;i++)
						{
							if(i%2)
								continue;
							
                           temp[i]=outbuf[j];
						   j++;
						}
						//temp[len]=0;
						//P1=(LPARAM)outbuf;
						//P2=len;// sizeof(outbuf)/sizeof(char);
						LOGI("password buffer length %d\n",P2);
						ret=-1;

					}
				}
			}
			break;
		case UCM_NEEDPASSWORD    :
			{
				LOGI("archive needs password \n");
				if(getPassWordID)
				{
					Environment* enviro=(Environment*)UserData;
					jstring jstr = (jstring) enviro->env->CallObjectMethod(enviro->obj, getPassWordID);
					if(jstr)
					{
                        char outbuf[128];
						//pass[0]='m';pass[1]='a';pass[2]='h';pass[3]='m';pass[4]='1';pass[5]='9';pass[6]='8';pass[7]='5';
						//char* outbuf="mahm1985";
						int len = enviro->env->GetStringLength( jstr);
						enviro->env->GetStringUTFRegion( jstr, 0, len, outbuf);
						LOGI("archive given password is %s with length %d\n",outbuf,len);
						char * temp=(char*)P1;
						for(int i=0;i<len;i++)
						{							
                           temp[i]=outbuf[i];
						}
						temp[len]=0;
						//P1=(LPARAM)outbuf;
						//P2=len;// sizeof(outbuf)/sizeof(char);
						LOGI("password buffer length %d\n",P2);
						ret=1;

					}
				}
			}
			break;
	}
  return ret;
}

int displayError(int error, const char *filename)
{
	int ret=0;
        switch(error)
        {
                case ERAR_END_ARCHIVE:
                LOGE("Unable to open %s, ERAR_END_ARCHIVE", filename);
				ret=-1;
                break;

                case ERAR_NO_MEMORY:
                LOGE("Unable to open %s, ERAR_NO_MEMORY", filename);
				ret=-2;
                break;

                case ERAR_BAD_DATA:
                LOGE("Unable to open %s, ERAR_BAD_DATA", filename);
				ret=-3;
                break;

                case ERAR_BAD_ARCHIVE:
                LOGE("Unable to open %s, ERAR_BAD_ARCHIVE", filename);
				ret=-4;
                break;

                case ERAR_UNKNOWN_FORMAT:
                LOGE("Unable to open %s, ERAR_UNKNOWN_FORMAT", filename);
				ret=-5;
                break;

                case ERAR_EOPEN:
                LOGE("Unable to open %s, ERAR_EOPEN", filename);
				ret=-6;
                break;

                case ERAR_ECREATE:
                LOGE("Unable to open %s, ERAR_ECREATE", filename);
				ret=-7;
                break;

                case ERAR_ECLOSE:
                LOGE("Unable to open %s, ERAR_ECLOSE", filename);
				ret=-8;
                break;

                case ERAR_EREAD:
                LOGE("Unable to open %s, ERAR_EREAD", filename);
				ret=-9;
                break;

                case ERAR_EWRITE:
                LOGE("Unable to open %s, ERAR_EWRITE", filename);
				ret=-10;
                break;

                case ERAR_SMALL_BUF:
                LOGE("Unable to open %s, ERAR_SMALL_BUF", filename);
				ret=-11;
                break;

                case ERAR_UNKNOWN:
                LOGE("Unable to open %s, ERAR_UNKNOWN", filename);
				ret=-12;
                break;

                case ERAR_MISSING_PASSWORD:
                LOGE("Unable to open %s, ERAR_MISSING_PASSWORD", filename);
				ret=-13;
                break;

                default:
                LOGE("Unable to open %s, unknown error: %d", filename, error);
				ret=-14;
        }
		ret=error;
		return ret;
}



JNIEXPORT jint JNICALL Java_com_aroma_unrartool_Unrar_RarOpenArchive
  (JNIEnv *env, jobject obj, jstring param2, jstring param3)
{
	//const char *filename = env->GetStringUTFChars(param2, NULL);
	char outbuf[255],extrPath[255];
	jstring jstr;
	int retresult=0;
	jclass cls = env->GetObjectClass(obj);
	jmethodID mid = env->GetMethodID(cls, "relayMessage", "(ILjava/lang/String;)V");
	if(mid == NULL)
		LOGE("Error retrieving methodID for %s \n", "relayMessage()");
	int len = env->GetStringLength( param2);
	env->GetStringUTFRegion( param2, 0, len, outbuf);
	len = env->GetStringLength( param3);
	env->GetStringUTFRegion( param3, 0, len, extrPath);
	LOGI("openning Archive: %s \n", outbuf);
	LOGI("==========================\n");
	LOGI("Extracting to :%s\n",extrPath);
	LOGI("==========================\n");
	RAROpenArchiveDataEx data;
    memset(&data, 0, sizeof(RAROpenArchiveDataEx));
	 memset(&environment, 0, sizeof(Environment));

    data.ArcName = (char*)outbuf;//filename;
    data.OpenMode = RAR_OM_EXTRACT;
	HANDLE handle = RAROpenArchiveEx(&data);
	if (handle && !data.OpenResult)
	{
		environment.env=env;
		environment.obj=obj;
		RARSetCallback(handle,UnRarCallBack,(LPARAM)&environment);
		
		bool firstcheck=true;
		RARHeaderDataEx header;
		memset(&header, 0, sizeof(RARHeaderDataEx));
        int headererror=0 ;     
		while ((headererror=RARReadHeaderEx(handle, &header)) == 0)
		{  
			if(mid != NULL)
			{
				jstr = env->NewStringUTF(header.FileName);
				if (jstr != NULL) {
						
					env->CallVoidMethod(obj, mid,0,jstr);
					env->DeleteLocalRef(jstr);
				}
				else
					LOGE("Unable to Create JString ,outofmemory error");/* out of memory */				   
			}
			int result = RARProcessFile(handle, RAR_EXTRACT, extrPath/*"/mnt/sdcard/unrartestfile"*/, NULL);
			if(firstcheck && (data.Flags & MHD_VOLUME))
			{
				LOGI("Archive is a Volume");
				if(header.UnpVer >=29 && (data.Flags & MHD_FIRSTVOLUME)==0)
				{
					LOGE("unrar from the wrong Volume");
					if(archivefirstVolume)
						env->SetBooleanField(obj, archivefirstVolume , false);
				}
				firstcheck=false;
			}

			if (result)
			{
				LOGE("Unable to process %s, error: %d", header.FileName, result);
				retresult=result;
				if(mid != NULL)
				{
					jstr = env->NewStringUTF(header.FileName);
					if (jstr != NULL) {
						
						env->CallVoidMethod(obj, mid,result,jstr);
						env->DeleteLocalRef(jstr);
					}
					else
	 					LOGE("Unable to Create JString ,outofmemory error");/* out of memory */				   
				}
				switch(result)
				{
					case ERAR_BAD_DATA :						
						break;
					case ERAR_BAD_ARCHIVE:						
						break;	
					case ERAR_UNKNOWN_FORMAT:						
						break;
					case ERAR_EOPEN:						
						break;
					case ERAR_ECREATE:						
						break;
					case ERAR_ECLOSE:						
						break;
					case ERAR_EREAD:						
						break;
					case ERAR_EWRITE:						
						break;
				}
			}
			else
			{
				//LOGI("Processing file: %s \n", header.FileName);
				/*if(mid != NULL)
				{
					jstr = env->NewStringUTF(header.FileName);
					if (jstr != NULL) {
						
						env->CallVoidMethod(obj, mid,0,jstr);
						env->DeleteLocalRef(jstr);
					}
					else
						LOGE("Unable to Create JString ,outofmemory error");			   
				}*/
			}
		}
        if(headererror==ERAR_BAD_DATA)	
		{
             LOGI("RARReadHeaderEx returned ERAR_BAD_DATA");
			 retresult= headererror;
		}
		if(headererror==ERAR_END_ARCHIVE)
			LOGI("RARReadHeaderEx returned ERAR_END_ARCHIVE");
		LOGI("RARReadHeaderEx returned %d",headererror);
		RARCloseArchive(handle);
	}
	else
	{
		printf("Error Code:%d \n",data.OpenResult);
		return displayError(data.OpenResult, outbuf);
		//return -1;
	}
	 //env->ReleaseStringUTFChars(param2, filename);

  LOGI("end of RarOpenArchive()");
  return retresult;
}

JNIEXPORT jint JNICALL Java_com_aroma_unrartool_Unrar_RarGetArchiveItems
  (JNIEnv *env, jobject obj, jstring fname)
{
	char outbuf[255];
	char *cmtBuff =NULL;
	jstring jstr;	
	int len = env->GetStringLength( fname);
	env->GetStringUTFRegion( fname, 0, len, outbuf);
	
	LOGI("openning Archive: %s  for listing\n", outbuf);
	LOGI("==========================\n");	
	RAROpenArchiveDataEx data;
    memset(&data, 0, sizeof(RAROpenArchiveDataEx));
	cmtBuff=(char*)malloc(MAX_COMMENT_SIZE);
	//memset(&environment, 0, sizeof(Environment));

    data.ArcName = (char*)outbuf;//filename;
	if(cmtBuff)
	{
	  data.CmtBuf=cmtBuff;
	  data.CmtBufSize=MAX_COMMENT_SIZE;
	}
    data.OpenMode = RAR_OM_LIST;
	HANDLE handle = RAROpenArchiveEx(&data);
	int itemscount=0;
	if (handle && !data.OpenResult)
	{
		/*environment.env=env;
		environment.obj=obj;
		RARSetCallback(handle,UnRarCallBack,(LPARAM)&environment);*/
		/* Create a new string and overwrite the instance field */
		if(data.Flags & MHD_SOLID)
		{
			LOGI("Archive is Solid");
			if(archivesolid)
				env->SetBooleanField(obj, archivesolid , true);
		}
		if(data.Flags & MHD_LOCK)
		{
			LOGI("Archive is LOCKED");
			if(archivelocked)
				env->SetBooleanField(obj, archivelocked , true);
		}
		if(data.Flags & MHD_AV)
		{
			LOGI("Archive is Signed");
			if(archivesigned)
				env->SetBooleanField(obj, archivesigned , true);
		}
		if(data.Flags & MHD_PROTECT)
		{
			LOGI("recovery Record is present");
			if(archiverecoveryRecord)
				env->SetBooleanField(obj, archiverecoveryRecord , true);
		}
		if(data.Flags & MHD_VOLUME)
		{
			LOGI("Archive is a Volume");
			if(archivevolume)
				env->SetBooleanField(obj, archivevolume , true);
		}
		if(data.CmtState == 1)
		{
			LOGI("Archive Comment read completely");
			jstring jstr = env->NewStringUTF( data.CmtBuf );
			if (jstr == NULL) 
			{
				LOGE("Unable to instanciate a new UTF String, may be an out of memory"); /* out of memory */
			}
			if(archiveCmtfid)
				env->SetObjectField(obj, archiveCmtfid , jstr);
			if(archivecommentPresent)
				env->SetBooleanField(obj, archivecommentPresent , true);
		}
		else
		{
			switch(data.CmtState)
			{
				case 0:
					LOGI("Archive Comments not present");
					if(archivecommentPresent)
						env->SetBooleanField(obj, archivecommentPresent , false);
					break;
				case ERAR_NO_MEMORY :
					LOGI("No enough memory to extract Archive Comments");
					break;
				case ERAR_BAD_DATA  :
					LOGI("Broken Archive Comments");
					break;
				case ERAR_UNKNOWN_FORMAT:
					LOGI("Unknown Archive comment format");
					break;
				case ERAR_SMALL_BUF:
					LOGI("Buffer too small,Archive comments not completely read");
					break;
			}
		}
		RARHeaderDataEx header;
		memset(&header, 0, sizeof(RARHeaderDataEx));
        int headererror=0 ; 
		
		while (headererror=RARReadHeaderEx(handle, &header) == 0)
		{  
			//RARSetPassword(handle,"mahm1985");
			int result = RARProcessFile(handle, RAR_OM_LIST, NULL/*"/mnt/sdcard/unrartestfile"*/, NULL);

			if (result)
			{
				LOGE("Unable to process %s, error: %d", header.FileName, result);
			}
			else
			{
				//LOGI("Processing file: %s \n", header.FileName);
				
			}
			itemscount++;
		}
        if(headererror==ERAR_BAD_DATA)
             LOGI("RARReadHeaderEx returned ERAR_BAD_DATA");
		if(headererror==ERAR_END_ARCHIVE)
			LOGI("RARReadHeaderEx returned ERAR_END_ARCHIVE");
		RARCloseArchive(handle);
	}
	else
	{
		printf("Error Code:%d \n",data.OpenResult);
		displayError(data.OpenResult, outbuf);
		return -1;
	}
	 //env->ReleaseStringUTFChars(param2, filename);

  LOGI("end of RarOpenArchive()");
  if(cmtBuff)
	  free(cmtBuff);
  return itemscount;
}
JNIEXPORT jint JNICALL Java_com_aroma_unrartool_Unrar_RarCloseArchive
  (JNIEnv *env, jobject, jint handle)
{
		return 0;
}

JNIEXPORT jint JNICALL Java_com_aroma_unrartool_Unrar_RarProcessArchive
  (JNIEnv *, jobject, jint handle, jstring dest)
{
	return 0;
}





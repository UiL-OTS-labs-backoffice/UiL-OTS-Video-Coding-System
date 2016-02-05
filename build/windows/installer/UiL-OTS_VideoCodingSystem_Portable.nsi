!include "UiLOTSDefinitions.nsh"
!define JRE_VERSION "1.6.0"

Name "UiL OTS Video Coding System"
Caption "UiL OTS Video Coding System Launcher"
Icon "logo.ico"
OutFile "${outLocation}/UiLOTS-VideoCodingSystem-${AppVersion}-Launcher.exe"

AutoCloseWindow true

Var JREPath
Var JreArchType
Var VLCPath

Section ""
	Push "${JRE_VERSION}"
	Call DetectJRE
	Push $0
	SetOutPath $TEMP
	File ${OutLocation}/${JarFile}
	Call CheckIfVLCInstalled
	StrCmp $VLCPath "" NoVLC Launch
NoVLC:
	CreateDirectory $TEMP\vlc-portable
	SetOutPath $TEMP\vlc-portable
	StrCpy $VLCPath $TEMP\vlc-portable
	IntCmp $JreArchType 64 X64 X32
X64:
	File /a /r "vlc-portable-win-64\"
	Goto Launch
X32:
	File /a /r "vlc-portable-win-32\"
	Goto Launch
Launch:
	Exec '$JREPath -jar "$TEMP\${JarFile}" -vlc="$VLCPath"'
SectionEnd


Function DetectJRE
  Exch $0	; Get version requested  
		; Now the previous value of $0 is on the stack, and the asked for version of JDK is in $0
  Push $1	; $1 = Java version string (ie 1.5.0)
  Push $2	; $2 = Javahome
  Push $3	; $3 and $4 are used for checking the major/minor version of java
  Push $4
  ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  StrCmp $1 "" DetectTry2
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$1" "JavaHome"
  StrCmp $2 "" DetectTry2
  Goto GetJRE
 
DetectTry2:
  ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  StrCmp $1 "" NoFound
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$1" "JavaHome"
  StrCmp $2 "" NoFound
 
GetJRE:
  IfFileExists "$2\bin\javaw.exe" 0 NoFound
  StrCpy $3 $0 1			; Get major version. Example: $1 = 1.5.0, now $3 = 1
  StrCpy $4 $1 1			; $3 = major version requested, $4 = major version found
  IntCmp $4 $3 0 FoundOld FoundNew
  StrCpy $3 $0 1 2
  StrCpy $4 $1 1 2			; Same as above. $3 is minor version requested, $4 is minor version installed
  IntCmp $4 $3 FoundNew FoundOld FoundNew
 
NoFound:
  MessageBox MB_OK "Java is not installed on this computer. Please install the latest version of Java"
  Abort
FoundOld:
  MessageBox MB_OK "Java version is too old. Please install the latest version of Java"
  Abort 
FoundNew:
   StrCpy $JREPath "$2\bin\javaw.exe"
   Call detectJreArchType
   Goto DetectJREEnd
DetectJREEnd:
	Pop $4
	Pop $3
	Pop $2
	Pop $1
	Exch $0
FunctionEnd

Function detectJreArchType
	Push $0
	DetailPrint "Checking the architecture type of the installed Java VM"
	File /oname=$TEMP\DetectJVM.jar DetectJVM.jar
	ExecWait '"$JREPath" -jar "$TEMP\DetectJVM.jar"' $0
	StrCpy $JreArchType $0
	Pop $0
FunctionEnd 

Function CheckIfVLCInstalled
	Exch $1 ; $1 base
	IntCmp $JreArchType 64 Check64Bit Check32Bit	
Check32Bit:
	ReadRegStr $1 HKLM "SOFTWARE\Wow6432Node\VideoLan\VLC" "InstallDir"
	Goto Compare
Check64Bit:
	ReadRegStr $1 HKLM "SOFTWARE\VideoLan\VLC" "InstallDir"
	Goto Compare
Compare:
	StrCpy $VLCPath $1 ""
	Exch $1
FunctionEnd

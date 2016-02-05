; Taken from http://nsis.sourceforge.net/Simple_installer_with_JRE_check by weebib
; Use it as you desire.
 
; Credit given to so many people of the NSIS forum.
 
!define AppName "UiL OTS Video Coding System"
!define AppVersion "2.1"
!define ShortName "VideoCodingSystem"
!define JRE_VERSION "1.6.0"
!define Vendor "UiL OTS Labs"
!define ShortVendor "UiLOTSLabs"
 
!include "MUI.nsh"
!include "Sections.nsh"
!include "FileAssociation.nsh"
!include "StrContains.nsh"
 
Var InstallJRE
Var JREPath
Var JreArchType

Var InstallVLC
Var VLCPath
 
;--------------------------------
;Configuration
 
  ;General
  Name "${AppName}"
  OutFile "setup.exe"
  Caption "Installing UiL OTS Video Coding System"
  Icon "logo.ico"
 
  ;Folder selection page
  InstallDir "$PROGRAMFILES\${SHORTNAME}"
 
  ;Get install folder from registry if available
  InstallDirRegKey HKLM "SOFTWARE\${ShortVendor}\${ShortName}" ""
  
  !define MUI_ICON "logo.ico"
 
; Installation types
;InstType "full"	; Uncomment if you want Installation types
 
;--------------------------------
;Pages
 
  ; License page
  !insertmacro MUI_PAGE_LICENSE "license.txt"
  ; This page checks for JRE. It displays a dialog based on JRE.ini if it needs to install JRE
  ; Otherwise you won't see it.
  Page custom CheckInstalledJRE
  
  ; This page checks the VLC installation with the same principle
  Page custom CheckIfVLCInstalled
 
  ; Define headers for the 'Java installation successfully' page
  !define MUI_INSTFILESPAGE_FINISHHEADER_TEXT "Java installation complete"
  !define MUI_PAGE_HEADER_TEXT "Installing Java runtime"
  !define MUI_PAGE_HEADER_SUBTEXT "Please wait while we install the Java runtime"
  !define MUI_INSTFILESPAGE_FINISHHEADER_SUBTEXT "Java runtime installed successfully."
  !insertmacro MUI_PAGE_INSTFILES
  !define MUI_INSTFILESPAGE_FINISHHEADER_TEXT "Installation complete"
  
  !define MUI_PAGE_HEADER_TEXT "Installing"
  !define MUI_PAGE_HEADER_SUBTEXT "Please wait while ${AppName} is being installed."
  !define MUI_PAGE_CUSTOMFUNCTION_PRE myPreInstfiles
  ; Uncomment the next line if you want optional components to be selectable
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_PAGE_FINISH
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
 
;--------------------------------
;Modern UI Configuration
 
  !define MUI_ABORTWARNING
 
;--------------------------------
;Languages
  !insertmacro MUI_DEFAULT MUI_PAGE_UNINSTALLER_PREFIX ""; fix warning message bug
  !insertmacro MUI_LANGUAGE "English"
 
;--------------------------------
;Language Strings
 
  ;Description
  LangString DESC_SecAppFiles ${LANG_ENGLISH} "Application files copy"
  LangString DESC_SecCreateShortcut ${LANG_ENGLISH} "Create shortcuts in the start menu to the application"
  LangString DESC_SecCreateDesktopShortcut ${LANG_ENGLISH} "Create a desktop shortcut to the application"
  LangString DESC_SecFileAssociation ${LANG_ENGLISH} "Associate all .UiL project files with the application"
 
  ;Header
  LangString TEXT_JRE_TITLE ${LANG_ENGLISH} "Java Runtime Environment"
  LangString TEXT_JRE_SUBTITLE ${LANG_ENGLISH} "Installation"
  LangString TEXT_PRODVER_TITLE ${LANG_ENGLISH} "Installed version of ${AppName}"
  LangString TEXT_PRODVER_SUBTITLE ${LANG_ENGLISH} "Installation cancelled"
 
;--------------------------------
;Reserve Files
 
  ;Only useful for BZIP2 compression
  ReserveFile "jre.ini"
  !insertmacro MUI_RESERVEFILE_INSTALLOPTIONS
 
;--------------------------------
;Installer Sections
 
Section -installjre jre
  Push $0
  Push $1
 
;  MessageBox MB_OK "Inside JRE Section"
  Strcmp $InstallJRE "yes" InstallJRE JREPathStorage
  DetailPrint "Starting the JRE installation"
InstallJRE:
  File /oname=$TEMP\jre_setup.exe jre-8u71-windows-i586.exe
  ;MessageBox MB_OK "Installing JRE"
  DetailPrint "Java not found. Launching JRE setup"
  ;ExecWait "$TEMP\jre_setup.exe /S" $0
  ; The silent install /S does not work for installing the JRE, sun has documentation on the 
  ; parameters needed.  I spent about 2 hours hammering my head against the table until it worked
  ExecWait "$TEMP\jre_setup.exe INSTALL_SILENT=1 REBOOT=0" $0
  DetailPrint "Setup finished"
  Delete "$TEMP\jre_setup.exe"
  StrCmp $0 "0" InstallVerif 0
  Push "The JRE setup has been abnormally interrupted."
  Goto ExitInstallJRE
 
InstallVerif:
  DetailPrint "Checking the JRE Setup's outcome"
;  MessageBox MB_OK "Checking JRE outcome"
  Push "${JRE_VERSION}"
  Call DetectJRE  
  Pop $0	  ; DetectJRE's return value
  StrCmp $0 "0" ExitInstallJRE 0
  StrCmp $0 "-1" ExitInstallJRE 0
  Goto JavaExeVerif
  Push "The JRE setup failed"
  Goto ExitInstallJRE
 
JavaExeVerif:
  ;MessageBox MB_OK "Checking if file $0 exists"
  IfFileExists $0 JREPathStorage 0
  Push "The following file : $0, cannot be found."
  Goto ExitInstallJRE
 
JREPathStorage:
  ;MessageBox MB_OK "Path Storage  1 = $1   and 0 = $0"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "jre.ini" "UserDefinedSection" "JREPath" $1
  StrCpy $JREPath $0
  Pop $0		; Restore $0
  Goto End
 
ExitInstallJRE:
  Pop $1
  MessageBox MB_OK "The setup was interrupted for the following reason : $1"
  Pop $1 	; Restore $1
  Pop $0 	; Restore $0
  Abort
End:
  Pop $1	; Restore $1
  Pop $0	; Restore $0
SectionEnd

Section -installvlc vlc
	Push $0
	Push $1
	DetailPrint "Probing VLC installation"
	StrCmp $InstallVLC "yes" InstallVLC
	${StrContains} $1 "x86" $VLCPath
	StrCmp $1 "" x64Detected x86Detected
x86Detected:
	DetailPrint "Detected 32 bit install of VLC, checking compatibility"
	IntCmp $JreArchType 64 InstallVLC EndOfFunction
x64Detected:
	DetailPrint "Detected 64 bit install of VLC, checking compatibility"
	IntCmp $JreArchType 64 EndOfFunction InstallVLC
InstallVLC:
	DetailPrint "Starting the VLC installation"
	IntCmp $JreArchType 64 X64 NotX64
X64:
	DetailPrint "Extracting VLC setup"
	File /oname=$TEMP\vlc_installer.exe vlcwin64.exe
	Goto FinishInstall
NotX64:
	DetailPrint "Extracting VLC setup"
	File /oname=$TEMP\vlc_installer.exe vlcwin32.exe
	Goto FinishInstall
FinishInstall:
	DetailPrint "Starting VLC setup"
	ExecWait "$TEMP\vlc_installer.exe /S" $0
	DetailPrint "Setup finished. Cleaning up files"
	Delete "$TEMP\vlc_installer.exe"
	DetailPrint "Verifying VLC installation"
	StrCmp $0 "0" VerifyInstallation ExitInstallVLC	
VerifyInstallation:
	Call CheckIfVLCInstalled
	StrCmp $VLCPath "NOTFOUND" StillNotFound EndOfFunction
	Goto EndOfFunction
ExitInstallVLC:
  Pop $1
  MessageBox MB_OK "The setup was interrupted for the following reason : $1"
  Pop $0
  Abort
StillNotFound:
	MessageBox MB_OK "The installation of VLC seemed to have failed. Install VLC manually and try again"
	Goto EndOfFunction
	Abort
EndOfFunction:
	Pop $1
	Pop $0
SectionEnd
 
Section "${AppName}" SecAppFiles
  SectionIn 1 RO	; Full install, cannot be unselected
			; If you add more sections be sure to add them here as well
  SetOutPath $INSTDIR
  File VideoCodingUiLOTS.jar
  File logo.ico
  ;Store install folder
  WriteRegStr HKLM "SOFTWARE\${Vendor}\${ShortName}" "" $INSTDIR
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${ShortName}" "DisplayName" "${AppName}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${ShortName}" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${ShortName}" "NoModify" "1"
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${ShortName}" "NoRepair" "1"
  
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"
  
  ;Set VLC path for system
  ExecWait '$JREPath -jar "$INSTDIR/VideoCodingUiLOTS.jar" setVLC="$VLCPath"'
SectionEnd
 
 
Section "Start menu shortcuts" SecCreateShortcut
  SectionIn 1	; Can be unselected
  CreateDirectory "$SMPROGRAMS\${AppName}"
  CreateShortCut "$SMPROGRAMS\${AppName}\Uninstall Video Coding System.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  CreateShortCut "$SMPROGRAMS\${AppName}\${AppName}.lnk" "$JREPath" '-jar "$INSTDIR\VideoCodingUiLOTS.jar"' "$INSTDIR\logo.ico" 0
; Etc
SectionEnd

Section "Desktop shortcut" SecCreateDesktopShortcut
	SectionIn 1 ; Can be unselected
	CreateShortCut "$DESKTOP\${AppName}.lnk" "$JREPath" '-jar "$INSTDIR\VideoCodingUiLOTS.jar"' "$INSTDIR\logo.ico" 0
SectionEnd

Section "Associate .UiL files" SecFileAssociation
	SectionIn 1;
	${registerExtension} "$JREPath" '-jar "$INSTDIR\VideoCodingUiLOTS.jar"' ".UiL" "UiL OTS Video Coding Project" "$INSTDIR\logo.ico"
SectionEnd
 
;--------------------------------
;Descriptions
 
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SecAppFiles} $(DESC_SecAppFiles)
  !insertmacro MUI_DESCRIPTION_TEXT ${SecCreateShortcut} $(DESC_SecCreateShortcut)
  !insertmacro MUI_DESCRIPTION_TEXT ${SecCreateDesktopShortcut} $(DESC_SecCreateDesktopShortcut)
  !insertmacro MUI_DESCRIPTION_TEXT ${SecFileAssociation} $(DESC_SecFileAssociation)
!insertmacro MUI_FUNCTION_DESCRIPTION_END
 
;--------------------------------
;Installer Functions
 
Function .onInit
 
  ;Extract InstallOptions INI Files
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "jre.ini"
  Call SetupSections
 
FunctionEnd
 
Function myPreInstfiles
 
  Call RestoreSections
  SetAutoClose true
 
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
	StrCmp $1 "" NotFound Found
NotFound:
	StrCpy $1 "NOTFOUND"
	StrCpy $InstallVLC "yes"
	Goto EndOfFunction
Found:
	StrCpy $VLCPath $1
	StrCpy $InstallVLC "no"
	Goto EndOfFunction
EndOfFunction:
	Exch $1 ; restore stack
	Return
FunctionEnd
 
Function CheckInstalledJRE
  ;MessageBox MB_OK "Checking Installed JRE Version"
  Push "${JRE_VERSION}"
  Call DetectJRE
  ;Messagebox MB_OK "Done checking JRE version"
  Exch $0	; Get return value from stack
  StrCmp $0 "0" NoFound
  StrCmp $0 "-1" FoundOld
  Goto JREAlreadyInstalled
 
FoundOld:
  ;MessageBox MB_OK "Old JRE found"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "jre.ini" "Field 1" "Text" "${AppName} requires a more recent version of the Java Runtime Environment than the one found on your computer.\
The installation of JRE ${JRE_VERSION} will start."
  !insertmacro MUI_HEADER_TEXT "$(TEXT_JRE_TITLE)" "$(TEXT_JRE_SUBTITLE)"
  !insertmacro MUI_INSTALLOPTIONS_DISPLAY_RETURN "jre.ini"
  Goto MustInstallJRE
 
NoFound:
  ;MessageBox MB_OK "JRE not found"
  !insertmacro MUI_INSTALLOPTIONS_WRITE "jre.ini" "Field 1" "Text" "No Java Runtime Environment could be found on your computer  \
The installation of JRE v${JRE_VERSION} will start."
  !insertmacro MUI_HEADER_TEXT "$(TEXT_JRE_TITLE)" "$(TEXT_JRE_SUBTITLE)"
  !insertmacro MUI_INSTALLOPTIONS_DISPLAY_RETURN "jre.ini"
  Goto MustInstallJRE
 
MustInstallJRE:
  Exch $0	; $0 now has the installoptions page return value
  ; Do something with return value here
  Pop $0	; Restore $0
  StrCpy $InstallJRE "yes"
  Return
 
JREAlreadyInstalled:
;  MessageBox MB_OK "No download: ${TEMP2}"
  ;MessageBox MB_OK "JRE already installed: $0"
  StrCpy $JREPath $0
  StrCpy $InstallJRE "no"
  Call detectJreArchType
  !insertmacro MUI_INSTALLOPTIONS_WRITE "jre.ini" "UserDefinedSection" "JREPath" $JREPATH
  ;Pop $0		; Restore $0
  Return
 
FunctionEnd
 
Function detectJreArchType
	Push $0
	DetailPrint "Checking the architecture type of the installed Java VM"
	File /oname=$TEMP\DetectJVM.jar DetectJVM.jar
	ExecWait '"$JREPath" -jar "$TEMP\DetectJVM.jar"' $0
	StrCpy $JreArchType $0
	Pop $0
FunctionEnd 
; Returns: 0 - JRE not found. -1 - JRE found but too old. Otherwise - Path to JAVA EXE
 
; DetectJRE. Version requested is on the stack.
; Returns (on stack)	"0" on failure (java too old or not installed), otherwise path to java interpreter
; Stack value will be overwritten!
 
Function DetectJRE
  Exch $0	; Get version requested  
		; Now the previous value of $0 is on the stack, and the asked for version of JDK is in $0
  Push $1	; $1 = Java version string (ie 1.5.0)
  Push $2	; $2 = Javahome
  Push $3	; $3 and $4 are used for checking the major/minor version of java
  Push $4
  ;MessageBox MB_OK "Detecting JRE"
  ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  ;MessageBox MB_OK "Read : $1"
  StrCmp $1 "" DetectTry2
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$1" "JavaHome"
  ;MessageBox MB_OK "Read 3: $2"
  StrCmp $2 "" DetectTry2
  Goto GetJRE
 
DetectTry2:
  ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
  ;MessageBox MB_OK "Detect Read : $1"
  StrCmp $1 "" NoFound
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$1" "JavaHome"
  ;MessageBox MB_OK "Detect Read 3: $2"
  StrCmp $2 "" NoFound
 
GetJRE:
; $0 = version requested. $1 = version found. $2 = javaHome
  ;MessageBox MB_OK "Getting JRE"
  IfFileExists "$2\bin\javaw.exe" 0 NoFound
  StrCpy $3 $0 1			; Get major version. Example: $1 = 1.5.0, now $3 = 1
  StrCpy $4 $1 1			; $3 = major version requested, $4 = major version found
  ;MessageBox MB_OK "Want $3 , found $4"
  IntCmp $4 $3 0 FoundOld FoundNew
  StrCpy $3 $0 1 2
  StrCpy $4 $1 1 2			; Same as above. $3 is minor version requested, $4 is minor version installed
  ;MessageBox MB_OK "Want $3 , found $4" 
  IntCmp $4 $3 FoundNew FoundOld FoundNew
 
NoFound:
  ;MessageBox MB_OK "JRE not found"
  Push "0"
  Goto DetectJREEnd
 
FoundOld:
  ;MessageBox MB_OK "JRE too old: $3 is older than $4"
;  Push ${TEMP2}
  Push "-1"
  Goto DetectJREEnd  
FoundNew:
	
  ;MessageBox MB_OK "JRE is new: $3 is newer than $4"
  Push "$2\bin\javaw.exe"
;  Push "OK"
;  Return
   Goto DetectJREEnd
DetectJREEnd:
	; Top of stack is return value, then r4,r3,r2,r1
	Exch	; => r4,rv,r3,r2,r1,r0
	Pop $4	; => rv,r3,r2,r1r,r0
	Exch	; => r3,rv,r2,r1,r0
	Pop $3	; => rv,r2,r1,r0
	Exch 	; => r2,rv,r1,r0
	Pop $2	; => rv,r1,r0
	Exch	; => r1,rv,r0
	Pop $1	; => rv,r0
	Exch	; => r0,rv
	Pop $0	; => rv
FunctionEnd
 
Function RestoreSections
  !insertmacro UnselectSection ${jre}
  !insertmacro UnselectSection ${vlc}
  !insertmacro SelectSection ${SecAppFiles}
  !insertmacro SelectSection ${SecCreateShortcut}
  !insertmacro SelectSection ${SecCreateDesktopShortcut}
  !insertmacro SelectSection ${SecFileAssociation}
FunctionEnd
 
Function SetupSections
  !insertmacro SelectSection ${jre}
  !insertmacro SelectSection ${vlc}
  !insertmacro UnselectSection ${SecAppFiles}
  !insertmacro UnselectSection ${SecCreateShortcut}
  !insertmacro UnselectSection ${SecCreateDesktopShortcut}
  !insertmacro UnselectSection ${SecFileAssociation}
FunctionEnd

;--------------------------------
;Uninstaller Section
 
Section "Uninstall"
  ; remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${ShortName}"
  DeleteRegKey HKLM  "SOFTWARE\${Vendor}\${AppName}"
  ${unregisterExtension} ".UiL" "UiL OTS Video Coding Project"
  
  ; remove shortcuts, if any.
    
  setShellVarContext current  
  Delete "$DESKTOP\${AppName}.lnk"
  RMDir /r "$SMPROGRAMS\${AppName}"
  
  setShellVarContext all
  Delete "$DESKTOP\${AppName}.lnk"
  RMDir /r "$SMPROGRAMS\${AppName}"  
  
  ; remove files
  RMDir /r "$INSTDIR"
SectionEnd

import os, sys, json, re
from collections import OrderedDict

version_name_check = "[\w\ \-\_]*"


def checkVersion(v):
    """
    Checks if version is of correct format
    """
    versions = v.split(".")
    if len(versions) != 3:
        raiseVersionError(v)
    try:
        major = int(versions[0])
        minor = int(versions[1])
        micro = int(versions[2])
        print("New version number succesfully parsed")
        return {
            "major": major,
            "minor": minor,
            "micro": micro,
            "version": "{0}.{1}.{2}".format(major, minor, micro)
        }
    except:
        raiseVersionError(v)


def raiseVersionError(v):
    # raise ValueError("Version '%s' incorrect. Should contain only numbers and exactly two dots" % v)
    print("Version '%s' incorrect. Should contain only numbers and exactly two dots" % v)
    exit(1)


def parseArgs():
    """
    Parsing command line arguments
    First argument is version number
    Second argument is version name (optional)
    """
    args = list(filter(lambda a: not a.startswith("-"), sys.argv))
    if len(args) > 0:
        runAfterwards = "-c" in sys.argv
        if len(args) > 1:
            version = checkVersion(args[1])
            print(version)
            if len(args) > 2:
                has_to_be = re.compile("^{0}$".format(version_name_check))
                if not re.match(has_to_be, args[2]):
                    # raise ValueError("Version name cannot contain any strange characters")
                    print("Version name can only contain letters, numbers, spaces, dashes and underscores")
                    exit(1)
                version["v_name"] = args[2]
        else:
            raiseVersionError("not provided")
    print("Version parsed")

    return version, runAfterwards


def updateConfig(v):
    """
    Updates the version in the config file
    """
    print("Reading config.json file")
    f = open("build/config.json", "rb")
    json_content = f.read()
    f.close()
    json_values = json.loads(json_content, object_pairs_hook=OrderedDict)
    json_values['version_major'] = v['major']
    json_values['version_minor'] = v['minor']
    json_values['version_micro'] = v['micro']
    json_values['version'] = v['version']
    json_values['output_location'] = "build/output/version-{version}".format(**v)
    json_values['jar_file'] = "VideoCodingUiLOTS-{version}.jar".format(**v)
    if "v_name" in v:
        json_values['version_name'] = v['v_name']
    json_dumped = json.dumps(json_values, indent=4)
    print("Updating config.json file")
    f = open("build/config.json", "w")
    f.write(json_dumped)
    f.close()
    print("Config.json file updated")
    return json_values


def updateJavaAbout(v):
    """
    Updates the 'about' panel in the Java project
    """
    check = False
    p = re.compile(
        'JLabel lblUilOtsVideo = new JLabel\("UiL OTS Video Coding System V(\d+\.\d+\.\d+ \(({0})\)\").*\n'.format(
            version_name_check))
    java_file = "src/view/panels/About.java"
    print("Reading Java '{0}' file".format(java_file))
    f = open(java_file, 'r')
    lst = list()
    for l in f.readlines():
        line, replaced = replaceLine(l, p, v)
        check = check or replaced
        lst.append(line)
    f.close()

    if check:
        print("Writing Java '{0}' file".format(java_file))
        f = open(java_file, 'w')
        for l in lst:
            f.write(l)
        f.close()
        print("Java project version updated")
    else:
        print("Java version was *NOT* updated, because the line containing the version could not be found")


def replaceLine(l, p, v):
    """
    Matches the line that contains the version with a regex, and replaces
    the version number and name with the newer version number and name
    """
    searched = re.search(p, l)
    if searched != None and len(searched.groups()) > 1:
        if "v_name" not in v:
            v["v_name"] = searched.group(2)
        version = "{version} ({v_name})".format(**v)
        subre = re.compile('\d+\.\d+\.\d+ \(({0})\)'.format(version_name_check))
        r = re.sub(subre, version, l)
        print("Line with version found")
        return r, True
    else:
        return l, False


def updateNsi(json):
    """
    Re create the definitions file for the NSI script
    """
    jarFilePath = getJarFileRelativeLocation(json)
    print("Updating NSIS definition file")
    f = open("build/installer/windows/UiLOTSDefinitions.nsh", 'w')
    f.write("!define OutLocation \"{0}\"\n".format(jarFilePath))
    f.write("!define JarFile \"{jar_file}\"\n".format(**json))
    f.write("!define AppVersion \"{version}\"\n".format(**json))
    f.write("!define VLC_Install_version \"{vlc_version}\"\n".format(**json))
    f.write("!define Java_Installer \"{java_installer}\"\n".format(**json))
    f.close()
    print("Updated NSIS definition file")


def getJarFileRelativeLocation(json):
    """
    Get the relative directory for the jar file, from the nsi scripts.
    If something changes in directory structure, this fails
    """
    curAbsPath = os.path.abspath(".")
    os.chdir("build/installer/windows/")
    relPathToJar = os.path.relpath(curAbsPath + "/" + json["output_location"])
    os.chdir(curAbsPath)
    return relPathToJar


def updateAnt(json):
    print("Updating ant")
    p = re.compile('<property name=\"([\w\.]*)\" value=\"([-${}\/\w\.]*)"\/>')
    f = open("build/build.xml", "r")
    lst = list()
    for l in f.readlines():
        lst.append(updateAntLine(json, p, l))
    f.close()
    print("Parameters changed")

    f = open("build/build.xml", 'w')
    for l in lst:
        f.write(l)
    f.close()
    print("Ant updated")


def updateAntLine(json, p, l):
    searched = re.search(p, l)
    if searched != None and len(searched.groups()) > 1:
        if searched.group(1) == "version":
            return '\t<property name="version" value="{version}"/>\n'.format(**json)
        elif searched.group(1) == "jarname":
            return '\t<property name="jarname" value="{jar_file}"/>\n'.format(**json)
        elif searched.group(1) == "output.dir":
            return '\t<property name="output.dir" value="output/version-${version}"/>\n'
    return l


def updateDebian(json, arch):
    print("Updating debian")
    p = re.compile(r'^Version: .*')
    f = open("build/installer/debian/control-%s" % arch, "r")
    lst = list()
    for l in f.readlines():
        lst.append(updateDebianLine(json, p, l))
    f.close()
    print("Changing version in debian package")
    f = open("build/installer/debian/control-%s" % arch, 'w')
    for l in lst:
        f.write(l)
    f.close()
    print("Version updated in debian control file.")


def updateDebianLine(json, p, l):
    searched = re.search(p, l)
    if searched != None:
        return "Version: {version}\n".format(**json)
    else:
        return l


def runAnt():
    os.chdir("build")
    os.system("ant")
    os.chdir("../")


if __name__ == "__main__":
    v, runAfterwards = parseArgs()
    json = updateConfig(v)
    updateJavaAbout(v)
    updateNsi(json)
    updateAnt(json)
    updateDebian(json, "amd64")
    updateDebian(json, "i386")
    if runAfterwards:
        runAnt()

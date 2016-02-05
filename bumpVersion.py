import os, sys, json, re
from collections import OrderedDict



def checkVersion(v):
	versions = v.split(".")
	if len(versions) != 3:
		raiseVersionError(v)
	try:
		major = int(versions[0])
		minor = int(versions[1])
		micro = int(versions[2])
		return {
			"major" : major,
			"minor" : minor,
			"micro" : micro,
			"version" : "{0}.{1}.{2}".format(major, minor, micro)
		}
	except:
		raiseVersionError(v)

def raiseVersionError(v):
	raise ValueError("Version '%s' incorrect. Should contain only numbers and exactly two dots" % v)

def parseArgs():
	if len(sys.argv) > 1:
		version = checkVersion(sys.argv[1])
		if len(sys.argv) > 2:
			if not re.match(ur'\w*', sys.argv[2]):
				raise ValueError("Version name cannot contain any strange characters")
			version["v_name"] = sys.argv[2]
	else:
		raiseVersionError("none")

	return version

def updateConfig(v):
	f = open("build/config.json", "rb")
	json_content = f.read()
	f.close()
	json_values = json.loads(json_content, object_pairs_hook=OrderedDict)
	json_values['version_major'] = v['major']
	json_values['version_minor'] = v['minor']
	json_values['version_micro'] = v['micro']
	if "v_name" in v:
		json_values['version_name'] = v['v_name']
	json_dumped = json.dumps(json_values, indent=4)
	
	f = open("build/config_test.json", "wb")
	f.write(json_dumped)
	f.close()

def updateJavaAbout(v):
	p = re.compile(ur'JLabel lblUilOtsVideo = new JLabel\("UiL OTS Video Coding System V(\d+\.\d+\.\d+ \((.*)\)\").*\n')
	java_file = "src/view/panels/About.java"
	f = open(java_file, 'rb')
	lst = list()
	for l in f.readlines():
		lst.append(replaceLine(l,p,v))
	f.close()
	f = open(java_file, 'wb')
	for l in lst:
		f.write(l)
	f.close()
	

def replaceLine(l,p,v):
	searched = re.search(p,l)
	if searched != None and len(searched.groups()) > 1:
		if "v_name" not in v:
			v["v_name"] = searched.group(2)
		version = "{version} ({v_name})".format(**v)
		r = re.sub(ur'\d+\.\d+\.\d+ \((\w*)\)', version, l)
		return r
	else:
		return l


if __name__ == "__main__":
	v = parseArgs()
	updateConfig(v)
	updateJavaAbout(v)
			





import sys
import chgender
import gender_guesser.detector as gender
from gender_detector import GenderDetector
reload(sys)
sys.setdefaultencoding('utf-8')

f = open("dblp_authors_ids", "r")
w = open("dblp_authors_ids_gender", "w")
d = gender.Detector()
detector = GenderDetector('us')


for line in f:
   id_, full_name = line.split("\t")
   full_name = full_name.strip()
   name = full_name.split()[0].strip()
   g = d.get_gender(name)
   g = "male" if g == "mostly_male" else "female" if g == "mostly_female" else g
   if g == "unknown" or g == "andy":
   	g1 = detector.guess(name) 
   	g = g1 if g1 != "unknown" else chgender.guess(name)[0]
   w.write(id_ + "\t" + name + "\t" + g.strip() + "\n")   
w.close()
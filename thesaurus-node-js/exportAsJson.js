var fs = require('fs')
var thesaurus = require('thesaurus')
let json = thesaurus.load('./th_en_US_new.dat').toJson()
fs.writeFile('thesaurus.json', json, 'utf8')

#!/usr/bin/ruby

require 'json'
require 'erb'
require 'cgi'


def getObjectDocs

	return `ls *.json`.split("\n").map{|file|

		begin

			JSON.parse(`cat #{file}`)
					
		rescue => e
			$stderr.puts file
			$stderr.puts e
			nil
		end
	}.select{|doc| doc != nil }

end

def mapdata? map
	
	map.each_pair{|k, v|
		return true if k != "" && v != ""
	}
	return false
	
end



template = `cat template.html`.gsub(/^\s*\%/, '%')

erb = ERB.new(template, 0, "%<>")

begin
	page = erb.result
rescue => e
	$stderr.puts e
end

puts page

package eu.appsatori.ts2011

import spock.lang.Shared;
import spock.lang.Specification;

import groovy.sql.Sql;

class CalcSpec extends Specification{

	
	@Shared sql = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")
	def calc = new Calc()
	
	def setup(){
		println "This is called before each feature method."
	}
	
	def cleanup(){
		println "This is called after each feature method."
	}
	
	def setupSpec(){
		sql.execute '''
			create table basics (
				id int primary key, 
				first int, 
				operator char(1), 
				second int, 
				result int);
		'''
		sql.execute '''
			insert into basics 
				(id, first, operator, second, result)
			values
				(1, 4, '+', 2, 6),
				(2, 4, '-', 2, 2),
				(3, 4, '/', 2, 2),
				(4, 4, '*', 2, 8)
		'''
	}
	
	def cleanupSpec(){
		sql.execute 'drop table basics'
	}
	
	def "Zero must be shown on new calulator's display"(){
		expect:
		calc
		calc.display == 0 as String
	}
	
	def "Pushing numbers multiple times must append number to the display"(){
		when:
		pushButtons numbers
		then:
		calc.display == result as String
		where:
		numbers | result
		[1,2,3] | 123
		[2,2]   | 22
		[0,1,2] | 12
	}
	
	def "Basic operators must work as expected"(){
		when:
		calc.push first as String
		calc.push operator as String
		calc.push second as String
		calc.push '='
		then:
		calc.display == result as String
		where:
		[first, operator, second, result] << sql.rows(
			'select first, operator, second, result from basics'
		)
	}
	
	def "Pushing operator pushes display to the registry"(){
		when: "I push 3 and 2"
		pushButtons([3,2])
		
		then: "the registry is empty"
		calc.registry == '0'
		
		and: "the display shows 32"
		calc.display  == '32'
		
		when: "I push plus sign"
		calc.push '+'
		
		then: "the registry is now 32"
		calc.registry == '32'
		
		and: "the display is the same"
		calc.display  == calc.registry
		
		when: "I push 64"
		pushButtons([6,4])
		
		and: "the equals sign"
		calc.push '='
		
		then: "the result 96 is shown"
		calc.display == '96'
		
		and: "is also pushed into the registry"
		calc.display  == calc.registry
	}
	
	def "Calc uses given operators at the right time"(){
		setup:
		Operator plus = Mock()
		Operators operators = Mock()
		
		calc.operators = operators
		
		when:
		pushButtons([1,2])
		then:
		0 * operators.get(_)
		
		when:
		calc.push '+'
		then:
		1 * operators.get('+') >> plus
		0 * plus.operate(_,_)
		
		when:
		pushButtons([1,2])
		then:
		0 * operators.get(_)
		0 * plus.operate(_,_)
		
		when:
		calc.push '='
		then:
		1 * plus.operate(12, 12) >> 24
	}
	
	
	def pushButtons(numbers){
		numbers.each {
			calc.push it as String
		}
	}
	
}

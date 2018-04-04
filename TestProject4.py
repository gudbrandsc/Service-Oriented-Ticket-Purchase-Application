import unittest, urllib2
import tests
import json as simplejson
import json
import sys
import threading




BASE_URL = "http://localhost:5000"

def verify_result(res):
	if(res != None ):
		print 'Passed'
	else:
		sys.exit('Script ended post Failed')

def create_user(username):
	data = {'username': username}
	url = ('%s/users/create' % (BASE_URL))
	req = urllib2.Request(url)
	req.add_header('Content-Type', 'application/json')
	response = urllib2.urlopen(req, json.dumps(data))

	if(response.getcode() == 200):
		data = simplejson.load(response)
		userid = data["userid"]
		return userid
	else:
		return None
		
def verify_new_user(username, userid):
	url = ('%s/users/%s' % (BASE_URL, userid))
	req = urllib2.Request(url)
	response = urllib2.urlopen(req)

	if(response.getcode() == 200):
		data = simplejson.load(response)
		response_name = data["username"]
		if(response_name == username):
			print 'Passed -> New user found with matching username'
		else:
			sys.exit('Failed -> response_name != username')
	else:
		sys.exit('Failed -> status != 200')


def create_event(userid, event_name, number_of_tickets):
	data = {
	'userid': userid,
	'eventname': event_name,
	'numtickets': number_of_tickets}

	url = ('%s/events/create' % (BASE_URL))
	req = urllib2.Request(url)
	req.add_header('Content-Type', 'application/json')
	response = urllib2.urlopen(req, json.dumps(data))

	if(response.getcode() == 200):
		data = simplejson.load(response)
		eventid = data["eventid"]
		return eventid
	else:
		return None	

def check_event(userid, eventid, number_of_tickets, eventname, purchased):
	url = ('%s/events/%s' % (BASE_URL, eventid))
	req = urllib2.Request(url)
	response = urllib2.urlopen(req)

	if(response.getcode() == 200):
		data = simplejson.load(response)
		response_eventid = data["eventid"]
		response_tickets = data["avail"]
		response_name = data["eventname"]
		response_purchased = data["purchased"]

		if((response_eventid == eventid) & (response_name == eventname) & (response_tickets == number_of_tickets) & (response_purchased == purchased)):
			print 'Passed -> Event found with correct data '
		else:
			sys.exit('Failed -> Json do not match expected values')
	else:
		sys.exit('Failed -> status != 200')


def purchase_tickets(eventid,userid, number_of_tickets):
	data = {'tickets': number_of_tickets}

	url = ('%s/events/%s/purchase/%s' % (BASE_URL,eventid,userid))
	req = urllib2.Request(url)
	req.add_header('Content-Type', 'application/json')
	response = urllib2.urlopen(req, json.dumps(data))

	if(response.getcode() == 200):
		return 1
	else:
		return None	

def verify_tickets(userid, eventid, tickets_purchased):
	url = ('%s/users/%s' % (BASE_URL, userid))
	req = urllib2.Request(url)
	response = urllib2.urlopen(req)

	if(response.getcode() == 200):
		data = simplejson.load(response)

		count = 0
		for element in data["tickets"]:
			if(element['eventid'] == eventid):
				count += 1
		if(count == tickets_purchased):
			print 'Passed -> Number of tickets matched number of purchased tickets found %d tickets' % count
		else:
			sys.exit('Failed -> tickets_purchased != tickets')
	else:
		sys.exit('Failed -> status != 200')

def transfere_tickets(eventid, userid, number_of_tickets, targetuser):
	data = {
	"eventid": eventid,
	"tickets": number_of_tickets,
	"targetuser": targetuser
	}

	url = ('%s/users/%s/tickets/transfer' % (BASE_URL, userid))
	req = urllib2.Request(url)
	req.add_header('Content-Type', 'application/json')
	response = urllib2.urlopen(req, json.dumps(data))

	if(response.getcode() == 200):
		return 1
	else:
		return None	

def worker():
	print 'started'

	username = 'Script user'
	event_name = 'Testevent'

	number_of_tickets = 99
	tickets_purchased = 0;

	#POST /users/create
	userid = create_user(username)
	verify_result(userid)

	#GET users/{userid}
	verify_new_user(username,userid)
	
	#POST /events/create
	eventid = create_event(userid, event_name, number_of_tickets)
	verify_result(eventid)
	
	#GET events/{eventid}
	check_event(userid,eventid,number_of_tickets, event_name, tickets_purchased)

	#POST /events/{eventid}/purchase/{userid} 
	purchase_success = purchase_tickets(eventid,userid, number_of_tickets)
	verify_result(purchase_success)


	tickets_purchased = number_of_tickets; # tickets_purchased = 99
	number_of_tickets = 0;

	if(purchase_success == 1):
		#GET events/{eventid} -> avail = 0 && purchased = 99
		check_event(userid, eventid, number_of_tickets , event_name, tickets_purchased)

		#GET users/{userid} -> check that the user has the correct number of tickets
		verify_tickets(userid, eventid, tickets_purchased)
		targetuser = create_user("targetuser")
		verify_result(targetuser)
		transfer_success = transfere_tickets(eventid, userid, number_of_tickets, targetuser)
		verify_tickets(targetuser, eventid, number_of_tickets)
		verify_tickets(userid, eventid, tickets_purchased)
	return



if __name__ == '__main__':
	threads = []
	for i in range(100):
		t = threading.Thread(target=worker())
    	t.start()

















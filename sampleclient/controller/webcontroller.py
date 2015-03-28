from flask import Flask
from flask import render_template
from flask import request
from flask import redirect
from flask import url_for

from flask_bootstrap import Bootstrap

import requests
import json


#################
# configuration #
#################

app = Flask(__name__,  template_folder='../templates')
Bootstrap(app)

json_api_string = "http://localhost:8080/api"


##########
# routes #
##########

@app.route('/add', methods = ['GET'])
def show_add_form():
	return render_template('add.html')

@app.route('/add', methods = ['POST'])
def add():
	link = request.form["link"]
	tags = {"tag" :request.form["tag"]}
	comments = {"comment": request.form["comment"]}

	headers = {'Content-type': 'application/json'}

	payload = {'link': link}
	json_payload = json.dumps(payload)

	r = requests.post(json_api_string + "/add", data=json.dumps(payload) \
					, headers=headers)
	print (r.text)
	return redirect(url_for('list'))

@app.route('/delete/<id>', methods = ['GET'])
def delete(id):
	request = requests.put(json_api_string + "/delete" + "/" + id)
	request_json = request.json()
	print request_json
	return redirect(url_for('list'))

@app.route('/', methods = ['GET'])
def list():
	request = requests.get(json_api_string + "/")
	hyperlinkList = request.json()
	# return render_template('index.html')
	return render_template('index.html', entries=hyperlinkList)

@app.route('/search/', methods =['POST'])
def search():
	query = request.form["query"]
	payload = {'query': query}

	headers = {'Content-Type': 'application/json'}

	srequest = requests.post(json_api_string + "/getallwithtag", \
						data=json.dumps(payload), headers=headers)

	hyperlinkList = srequest.json()
	return render_template('search.html', entries=hyperlinkList, query=query)


if __name__ == '__main__':
	# request = requests.get("http://api.openweathermap.org/data/2.5/weather?q=London,uk")
	# request_json = request.json()
	# print(request_json['coord'])
	# print (request_json['coord']['lat'])
	app.debug = True
	app.run()
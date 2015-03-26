from flask import Flask
from flask import render_template
from flask import request
from flask import redirect
from flask import url_for

from flask_bootstrap import Bootstrap

import requests
import json


app = Flask(__name__,  template_folder='../templates')
Bootstrap(app)

json_api_string = "http://localhost:8080/api"


# @app.route('/')
# def home():
# 	return render_template('index.html')

# Method related to the External API
@app.route('/add', methods = ['GET'])
def show_add_form():
	return render_template('add.html')

@app.route('/add', methods = ['POST'])
def add():
	link = request.form["link"]
	tags = [{"tag" :request.form["tag"]}]
	comments = [{"comment": request.form["comment"]}]
	print link
	print tags
	print comments

	payload = {'link': link}
	json_payload = json.dumps(payload)
	print json_payload
	r = requests.post(json_api_string + "/add", json_payload)
	print r.status_code
	return redirect(url_for('list'))

@app.route('/modify/<id>')
def modify():
	request = requests.get(json_api_string + "/string")
	request_json = request.json()

	return render_template('modify.html', entry=request_json)

@app.route('/', methods = ['GET'])
def list():
	request = requests.get(json_api_string + "/")
	hyperlinkList = request.json()
	# return render_template('index.html')
	return render_template('index.html', entries=hyperlinkList)


if __name__ == '__main__':
	# request = requests.get("http://api.openweathermap.org/data/2.5/weather?q=London,uk")
	# request_json = request.json()
	# print(request_json['coord'])
	# print (request_json['coord']['lat'])
	app.debug = True
	app.run()
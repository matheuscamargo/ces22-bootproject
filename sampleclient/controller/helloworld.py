from flask import Flask
from flask import render_template

app = Flask(__name__, template_folder='../templates')

@app.route('/<username>')
def hello_world(username):
    return render_template('index.html', name=username)

@app.route('/')
def index():
	return render_template('index.html')

if __name__ == '__main__':
	app.debug = True
	app.run()
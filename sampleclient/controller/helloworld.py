from flask import Flask
from flask import render_template

app = Flask(__name__,  template_folder='../templates')

@app.route('/<username>')
def hello_world(username):
    return render_template('index.html', name=username)

@app.route('/')
def home():
	return render_template('index.html')


# Method related to the External API
@app.route('/add')
def add():
	return render_template('add.html')

@app.route('/modify')
def modify():
	return render_template('modify.html')

@app.route('/list')
def list():
	return render_template('list.html')


if __name__ == '__main__':
	app.debug = True
	app.run()
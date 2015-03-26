class Hyperlink(object):
	
	def __init__(self):
		self.link = ""
		self.lists = []
		self.comments = []

	def __init__(self, link, lists, comments):
		self.link = link
		self.lists = lists
		self.comments = comments

	def __str__(self):
		return "Hyperlink: %s" %link
from urllib.request import urlopen
from bs4 import BeautifulSoup

response = urlopen("https://map.naver.com/v5/search/%EC%9D%8C%EC%8B%9D%EC%A0%90?c=15,0,0,0,dh")
# 크롤링하고자하는 사이트

soup = BeautifulSoup(response, "html.parser")  # html에 대하여 접근할 수 있도록
print(soup)

# value = soup.find("div", {"class": "category-title"})
# print(value)

# value2 = value.text.strip()
# print(value2[0:4])




# import requests
# from bs4 import BeautifulSoup

# response = requests.get("https://map.naver.com/v5/search/%EC%9D%8C%EC%8B%9D%EC%A0%90?c=15,0,0,0,dh")
# html_data = BeautifulSoup(response.text, 'html.parser') 
# program_names = html_data.select('li>div>a>div>div>span')

# #반복문을 사용해서 각 태그의 텍스트값만 출력
# for tag in program_names:
# 	print(tag.get_text())




# from bs4 import BeautifulSoup 
# from selenium import webdriver
# import time

# query_txt = input('크롤링할 키워드는 무엇입니까?: ')

# path = "C:\chromedriver_win32/chromedriver.exe"
# driver = webdriver.Chrome(path)

# driver.get("https://www.naver.com/")
# time.sleep(2)

# driver.find_element_by_id("query").click()
# element = driver.find_element_by_id("query")
# element.send_keys(query_txt)

# driver.find_element_by_id("search_btn").click()
import psycopg2
import json

with open('credentials.json', 'r') as c:
    credentials = json.load(c)

with open('../candidatos/120000633197.json', 'r') as candidate:
    c_infos = json.load(candidate)


attr = ''
attr_type = ''
to_query = ''
for info in c_infos:
    attr = info
    if type(c_infos[info]) == bool:
        attr_type = 'BOOLEAN NOT NULL'
        to_query += attr + " " + attr_type + ", "
    elif type(c_infos[info]) == int:
        attr_type = 'INTEGER NOT NULL'
        to_query += attr + " " + attr_type + ", "
    elif type(c_infos[info]) == str:
        attr_type = 'VARCHAR NOT NULL'
        to_query += attr + " " + attr_type + ", "
    elif type(c_infos[info]) == float:
        attr_type = 'FLOAT NOT NULL'
        to_query += attr + " " + attr_type + ", "
    elif c_infos[info] is None:
        attr_type = 'VARCHAR'
        to_query += attr + " " + attr_type + ", "
    # elif type(c_infos[info]) == dict:
    #     attr_type = 'Dict'
    #     to_query += attr + " " + attr_type + ", "
    # elif type(c_infos[info]) == list:
    #     attr_type = 'List'
    #     to_query += attr + " " + attr_type + ", "

to_query = to_query[:-2]
query = "CREATE TABLE " + "candidatos (" + to_query + ");"

try:
    conn = psycopg2.connect(
        database=credentials["database"],
        user=credentials["user"],
        password=credentials["password"],
        host=credentials["host"],
        port=credentials["port"]
    )
    print("Connected to postgresql DB")
except:
    print("Failed to connect Database")

cur = conn.cursor()
try:
    cur.execute(query)
    print("Query Success")
except:
    print("Query Failed")

conn.commit()
cur.close()
conn.close()

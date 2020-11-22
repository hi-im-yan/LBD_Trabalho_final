import psycopg2
import json

with open('credentials.json', 'r') as c:
    credentials = json.load(c)


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
    cur.execute(
        "CREATE TABLE testando (id serial PRIMARY KEY, num integer, data varchar);")
    print("Query Success")
except:
    print("Query Failed")

conn.commit()
cur.close()
conn.close()

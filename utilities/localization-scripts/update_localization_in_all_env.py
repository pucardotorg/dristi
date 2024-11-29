import csv
import requests
import os

# Prompt user to input environment details
env_list = []
print("Enter environment details (type 'done' to finish):")
while True:
    env_url = input("Environment URL (e.g., dristi-kerala-dev.pucar.org): ").strip()
    if env_url.lower() == 'done':
        break
    username = input(f"Username for {env_url}: ").strip()
    password = input(f"Password for {env_url}: ").strip()
    tenant_id = input(f"Tenant ID for {env_url} (e.g., kl): ").strip()
    env_list.append({"url": env_url, "username": username, "password": password, "tenantId": tenant_id})

if not env_list:
    print("No environments provided. Exiting.")
    exit()

# Prompt user for file details
file_data = []
print("Enter file details for localization (type 'done' to finish):")
while True:
    filename = input("CSV filename (e.g., home.csv): ").strip()
    if filename.lower() == 'done':
        break
    if not os.path.isfile(filename):
        print(f"File {filename} does not exist. Please check the path and try again.")
        continue
    module = input(f"Module for {filename}: ").strip()
    file_data.append({'filename': filename, 'module': module})

if not file_data:
    print("No files provided for localization. Exiting.")
    exit()

language = input("Enter language code (default: en_IN): ").strip() or "en_IN"

# Headers for authentication and localization
headers = {
    'accept': 'application/json, text/plain, */*',
    'accept-language': 'en-US,en;q=0.7',
    "Authorization": "Basic ZWdvdi11c2VyLWNsaWVudDo=",
    'content-type': 'application/x-www-form-urlencoded',
    'user-agent': 'Mozilla/5.0'
}

localization_headers = {
    'Content-Type': 'application/json'
}

# Authentication data template
data = {
    'userType': 'EMPLOYEE',
    'scope': 'read',
    'grant_type': 'password'
}

def get_auth_token(url, headers, data):
    """Fetch authentication token for the environment."""
    try:
        response = requests.post(url, headers=headers, data=data)
        response.raise_for_status()
        token = response.json().get('access_token')
        if not token:
            raise ValueError("Authentication response did not contain a token.")
        return token
    except requests.exceptions.RequestException as e:
        print(f"Error fetching token for {url}: {e}")
        return None
    except ValueError as ve:
        print(ve)
        return None

# Fetch tokens for all environments
for index, env in enumerate(env_list):
    env_url = f"https://{env['url']}/user/oauth/token"
    data['username'] = env['username']
    data['password'] = env['password']
    data['tenantId'] = env['tenantId']
    print(f"Fetching token for environment: {env['url']}")
    token = get_auth_token(env_url, headers, data)
    if token:
        env['token'] = token
        print(f"Token fetched successfully for {env['url']}.")
    else:
        print(f"Failed to fetch token for {env['url']}. Skipping this environment.")
        env_list[index]['token'] = None

# Remove environments without tokens
env_list = [env for env in env_list if env.get('token')]

if not env_list:
    print("No environments with valid tokens. Exiting.")
    exit()

# Process each localization file and upload messages
for file_module in file_data:
    try:
        with open(file_module['filename'], mode='r') as file:
            csv_reader = csv.reader(file)
            module = f"rainmaker-{file_module['module']}"
            for index, row in enumerate(csv_reader):
                # Skip invalid or unwanted rows
                if len(row) < 2:
                    print(f"Skipping invalid row {index}: {row}")
                    continue

                text = str(row[1]).strip()
                if "{" in text or text in ('-', "NA"):
                    print(f"Skipping row {index} with invalid text: {row}")
                    continue

                if row[0].strip() and text:
                    for env in env_list:
                        loc_url = f'https://{env["url"]}/localization/messages/v1/_upsert'
                        loc_data = {
                            "RequestInfo": {"authToken": env['token']},
                            "tenantId": env['tenantId'],
                            "messages": [{
                                "code": row[0].strip(),
                                "message": text,
                                "module": module,
                                "locale": language
                            }]
                        }
                        try:
                            response = requests.post(loc_url, headers=localization_headers, json=loc_data)
                            response.raise_for_status()
                            print(f"Uploaded: {row[0].strip()} to {env['url']} for tenantId {env['tenantId']}.")
                        except requests.exceptions.RequestException as e:
                            print(f"Error uploading {row[0]} to {env['url']} for tenantId {env['tenantId']}: {e}")
    except FileNotFoundError:
        print(f"File not found: {file_module['filename']}")
    except Exception as e:
        print(f"An unexpected error occurred while processing {file_module['filename']}: {e}")

print("Localization upload process completed.")

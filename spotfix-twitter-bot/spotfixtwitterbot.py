import firebase_admin
from firebase_admin import credentials, db
import tweepy
from better_profanity import profanity
import os

cred = credentials.Certificate('../FirebaseServiceAccount.json') # Download Service Account JSON file from the Firebase console and save the file in the same directory as the bot.
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://database-url.firebaseio.com/'
})

api_key = 'TWITTER_API_KEY'
api_secret_key = 'TWITTER_API_SECRET_KEY'
access_token = 'TWITTER_ACCESS_TOKEN'
access_token_secret = 'TWITTER_ACCESS_TOKEN_SECRET'
bearer_token = 'TWITTER_BEARER_TOKEN'

client = tweepy.Client(
    bearer_token=bearer_token,
    consumer_key=api_key,
    consumer_secret=api_secret_key,
    access_token=access_token,
    access_token_secret=access_token_secret
)

profanity.load_censor_words()

hashtags_dict = {
    'water': ["#WaterIssue", "#LocalIssue", "#WaterCrisis", "#Community"],
    'electricity': ["#PowerOutage", "#ElectricityIssue", "#LocalIssue", "#Community"],
    'construction': ["#ConstructionIssue", "#LocalIssue", "#Building", "#Community"],
    'sanitation': ["#SanitationIssue", "#Cleanliness", "#LocalIssue", "#Community"],
}

def post_tweets(category):
    ref = db.reference(category)
    issues = ref.get()
    
    if issues:
        for key, data in issues.items():
            name = data.get('name')
            latitude = data.get('latitude')
            longitude = data.get('longitude')
            desc = data.get('desc')
            status = data.get('status')  

            if status == 'unsolved':
                if not profanity.contains_profanity(desc):
                    hashtags = " ".join(hashtags_dict.get(category, ["#LocalIssue"]))
                    tweet = f"⚠️ NEW ISSUE REPORTED ⚠️\n\n{desc} \n\nLocation: {latitude}, {longitude} \n\n{hashtags}"
                    
                    try:
                        response = client.create_tweet(text=tweet)
                        print(f"Tweet posted successfully! Tweet ID: {response.data['id']}")
                    except tweepy.TweepyException as e:
                        print(f"Error posting tweet: {e}")
                else:
                    print(f"Profanity detected in issue {key}: {desc}")
            else:
                print(f"Issue {key} is already solved. Skipping...")

categories = ['water', 'electricity', 'construction', 'sanitation']
for category in categories:
    post_tweets(category)
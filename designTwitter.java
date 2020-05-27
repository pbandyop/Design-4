// Time Complexity :
// Space Complexity :
// Did this code successfully run on Leetcode : Yes
// Any problem you faced while coding this : After class solution


// Your code here along with comments explaining your approach


//1.  In order to design Twitter, we need a global timestamp, HashMap to map users to tweets, Hashmap to map users to
//    their followers.
//2.  Just to avoid null checks, we call isFirstTime() method to initialize user's info if not present already.

class Twitter {
    
    //Extra Global Space: O((users * tweets) + (users * followers))
//                    where tweets is maximum number of tweets a particular user has,
//                    where followers is maximum number of followers a particular user has.
    
    int timestamp; //record the time of creation
    int feed; //number of top K feeds needed by the getNewsFeed function
    
    Map<Integer, List<Tweet>> userTweets;
    Map<Integer, Set<Integer>> userFollowers;
    
    /** Initialize your data structure here. */
    public Twitter() {
        timestamp = 0;
        feed = 10;
        
        userTweets = new HashMap<>();
        userFollowers = new HashMap<>();
    }
    
    /** Compose a new tweet. */
    public void postTweet(int userId, int tweetId) {
        
        //   Time Complexity:    O(1)
        //   Space Complexity:   O(1) --> refer global space
        
        //create a valid entry in both the maps for given user
        isFirstTime(userId);
        
        //get the list of tweets and add new tweetId for given user
        List<Tweet> tweet = userTweets.get(userId);
        tweet.add(new Tweet(tweetId, timestamp++));
    }
    
    /** Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user herself. Tweets must be ordered from most recent to least recent. */
    public List<Integer> getNewsFeed(int userId) {
              //  Time Complexity:    O(NlogN) -> N is total tweets user and its followers have
       //   Space Complexity:   O(N)     -> N is total tweets user and its followers have
        
        //create a valid entry in both the maps for given user
        isFirstTime(userId);
        
        Comparator<Tweet> customComparator = new Comparator<>(){
          public int compare(Tweet a, Tweet b){
            return a.createdAt - b.createdAt;  
          }  
        };
        
        //we will use priority queue to keep track of top 10 tweet ids w.r.t. time of creation
        PriorityQueue<Tweet> pq = new PriorityQueue<>(customComparator);
        
        //find list of followers for given user
        Set<Integer> followees = userFollowers.get(userId);
        
        //for each follower, find their corresponding tweets
        for(int followee: followees){
            List<Tweet> U_tweets = userTweets.get(followee);
            
            //add each tweet to pq to compare according to timestamp
            for(Tweet tweet: U_tweets){
                pq.add(tweet);
                
                if(pq.size() > feed){
                    pq.poll();
                }
            }
        }
        
        LinkedList<Integer> solution = new LinkedList<>();
        
        while(!pq.isEmpty()){
            solution.addFirst(pq.poll().tweetId);
        }
        
        return solution;
    }
    
    /** Follower follows a followee. If the operation is invalid, it should be a no-op. */
    public void follow(int followerId, int followeeId) {
         //   Time Complexity:    O(1)
        //   Space Complexity:   O(1) --> refer global space
        
        //check if both the follower and followee exists. If not, add them
        isFirstTime(followerId);
        isFirstTime(followeeId);
        
        //add the followee to followerId
        //follow(1,2) user 1 follows user 2
        userFollowers.get(followerId).add(followeeId);
    }
    
    /** Follower unfollows a followee. If the operation is invalid, it should be a no-op. */
    public void unfollow(int followerId, int followeeId) {
        
        //   Time Complexity:    O(1)
        //   Space Complexity:   O(1) --> refer global space
        
        //check if both the follower and followee exists. If not, add them
        isFirstTime(followerId);
        isFirstTime(followeeId);
        
        //remove the mapping
        if(followerId != followeeId){
           userFollowers.get(followerId).remove(followeeId); 
        }
    }
    
    private void isFirstTime(int userId){
        
        //   initialize user's info if not present already
        //   Time Complexity:    O(1)
        //   Space Complexity:   O(1) --> refer global space
        
        //check if user exists in user-tweet map
        if(!userTweets.containsKey(userId)){
            userTweets.put(userId, new LinkedList<>());
        }
        
        //check if user exists in user-follow map 
        if(!userFollowers.containsKey(userId)){
            userFollowers.put(userId, new HashSet<>());
            userFollowers.get(userId).add(userId);
        }
    }
}

class Tweet{
    
    int tweetId;
    int createdAt;
    
    Tweet(int tweetId, int createdAt){
        this.tweetId = tweetId;
        this.createdAt = createdAt;
    }
    
}

/**
 * Your Twitter object will be instantiated and called as such:
 * Twitter obj = new Twitter();
 * obj.postTweet(userId,tweetId);
 * List<Integer> param_2 = obj.getNewsFeed(userId);
 * obj.follow(followerId,followeeId);
 * obj.unfollow(followerId,followeeId);
 */
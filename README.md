# Project Name
CatFinder

# Problem/Solution
It can be overwhelming to be a first time pet owner. Our program is designed to simplify the process so that more people wanting pets can get cats without fear of not knowing enough. Our program lists multiple websites listings, allows users to save cats for later, and also has a new owners information page that tells owners all they need to know in a very digestable, easy to reference way. 

# Notices
The explore page does take time to load! Give it around 5 minutes, they'll appear!! The website also takes a minute or two to launch, so if it doesn't load into localhost:8080 immediately, give it a second.

# How To Use
MANUAL SET UP: Our project requires Java 26, spring-boot, and Maven 3.9.16. Then, with all these downloaded, go into the project terminal in whatever IDE you open it with and type in ".\mvnw spring-boot:run". After this launches (it will take a minute), go in your browser and go to localhost:8080. 

Java 26 - https://www.oracle.com/java/technologies/downloads/
Maven 3.9.16 - https://maven.apache.org/download.cgi

# REFLECTION - Plans We Never Got To
Filter System - initially, we were going to have a filter system to allow an easy way for the user to narrow down results. We didn't have time to do this and, with how the scraper is built, we didn't know how to efficiently achieve this goal.

Matchmaker game - we were going to implement a game to narrow down cats between all your saved cats and/or to pick out one cat of all the available ones for the user. We simply just ran out of time for this idea.

Location Based Search - We were planning on having the cats appear be based off an entered location, but we ran out of time.

# REFLECTION - Achieved Goals
EXPLORE, Kitty Scraper - Probably the most complicated part of our project, our team member Leah had to learn how to create a webscraper from scratch in order to get all the listings to appear on the site. Cats appear in little trading card style boxes with information pulled from their respective shelter sites. The web scraper goes site by site pulling the cats name, picture, age, cost, breed and link.

Save System - Implemented by Chris, the saving system allows users to save cats they see and these saved cats will stay between uses (so long as it's in the same browser). The system saves the cats information to localStorage so it persists between sessions.

New Owners Information - Implemented by Chris, although this section is probably the easiest coded one, it just being html for the most part, we felt it very important to include. The scariest aspect of adopting a new pet is not knowing how to take care of them/introduce them properly. We made sure to format it in a way that is easy to come back to and very easy to digest. 

# VIDEO LINK
https://youtu.be/lC49UGwIz7E

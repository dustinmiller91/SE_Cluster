# SE_Cluster

Basic clustering algorithm and UI for tracking down other players in the game Space Engineers.

Since the survival update the spawn mechanics in Space Engineers have been pretty broken. Given the scarcity of starting resources, it is far more lucrative and efficient to simply raid another player. Lucky for us, the new spawn mechanics facilitate this (with a bit of work).

Players now always spawn in a random location within 50km of another active player. This means that if we repeately spawn in space we can can cluster our coordinates and calculate the euclidean centroid, which will give us a reasonably close approximation of a player's location.

This is a quick and dirty clustering algorithm, grouping points that are all within 100km of one another. It then calculates each cluster's centroid (an xyz coordinate) and prints it (or displays it in our UI). Fun stuff.

Known issues. If you take too long to collect your spawn points they will be innacurrate as players move around. Additionally sometimes clusters can overlap, which throws things off. Space is huge though, so this is fairly rare.

Maybe if enough people start exploiting this system Keen will finally be incentivized to fix it. Yay!

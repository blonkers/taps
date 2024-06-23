# Taps

A super-simple life enhancement mod. Tired of your favorite villagers dying without fanfare? Didn't know you had a zombie on the loose in your new village?

Taps solves all of these problems. Notify all players upon the death of any villager, including their profession.

In honor of your highly valued villagers, the song Taps is played for any villager that dies who you previously traded with.

## Development

This mod uses [Fabric](https://fabricmc.net/wiki/tutorial:start).

Create a testable jar using:

```
./gradlew build
```

### Tips

#### Spawn a level 2 villager

```
/summon villager ~ ~1 ~ {VillagerData:{profession:fisherman,level:2,type:snow},PersistenceRequired:1,CustomName:"Harold"}
```

#### Larger window size

You can make the default Minecraft larger by default on every launch by updating `taps/run/options.txt`:

```
overrideWidth:1280
overrideHeight:720
```

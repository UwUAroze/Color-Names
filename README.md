# Color names

This library's primary purpose is to be able to specify a color and end up with a fitting name for that color 🌈

## Examples:<br>
`"#ffffff"` -> `White`<br>
`"#facfea"` -> `Classic Rose`<br>
`#abcdef` -> `Alphabet Blue`<br>
`#123456` -> `Incremental Blue`<br>
`#c1b2a3` -> `Balanced Beige`<br>
*...and so on*

## Where do the names come from?!
Color Names is meant to act as an easy drop-in dependency you can import and start using, meaning it's already bundled with a list of color names, a list that is maintained by another awesome open-sourced project: [meodai/color-names](https://github.com/meodai/color-names/). There's around 16.7 million sRGB colors, obviously not all of these are named, but this list provides plenty to work with, we can just extrapolate the closest color from the list.
<br><br>
If you'd like to make changes to the default list, consider reviewing their [naming rules](https://github.com/meodai/color-names/blob/master/CONTRIBUTING.md) and contributing there. Alternatively, you can fork this project yourself and replace/modify the list in `src/main/resources/colornames.csv` with whatever you'd like.

## How about performance, and how accurate is the "nearest" color?
The default colour list has over 30,000 names (that's a lot). Trying to find the closest color by comparing distance in the 3D color space can be pretty computationally expensive.
<br><br>
Other libraries with similar functionality seem to often approach this by iterating over all the colors, plotting the sRGB values and calculating [Euclidian distance](https://en.wikipedia.org/wiki/Euclidean_distance) and whatever has the lowest distance is the "closest" color. This has 2 notable concerns:
<br>
1. The sRGB color space isn't all that accurate in terms of visual similarity, ie: 3 sRGB values that are equally apart in terms of raw numerical value are unlikely to be visually "different" by the same factor. Okay... so, how do we put a number on the visual similarity of colors? Fortunately, that's not my job. The [CIELAB Color Space](https://en.wikipedia.org/wiki/CIELAB_color_space) has us covered! This color space precisely revolves around positioning colors with uniform visual perception and for this reason, its used for all sorts of color correction work, and is exactly what we need. Perfect, we convert our values from the sRGB color space to the CIELAB color space, problem one solved!
2. Iterating through >30,000 vectors in a 3D space and finding the distance between all of them to a point is... a lot of calculations. But it's exactly what we need, since that's how we find the [Delta-E](https://en.wikipedia.org/wiki/Color_difference#CIELAB_%CE%94E*) variance between all our CIELAB colours to see whats the closest. So, we should really try to optimise this. For this we cache our colors in a [K-D Tree](https://en.wikipedia.org/wiki/K-d_tree) with 3 dimensions, providing us with fast [nearest neighbour searches](https://en.wikipedia.org/wiki/Nearest_neighbor_search). This takes the time complexity for searches from O(n) to O(logn). In practice, this makes a pretty substantial difference.

### Benchmarks:
| 10 lookups - size of list:   | Linear approach | K-D Tree approach |   | 30,000 colors - No. of lookups: | Linear approach | K-D Tree approach |
|------------------------------|-----------------|-------------------|---|----------------------------------|-----------------|-------------------|
| 1,000                        | 0.027ms         | 0.0088ms          |   | 1                                | 0.17ms          | 0.009ms           |
| 10,000                       | 0.23ms          | 0.012ms           |   | 10                               | 1.05ms          | 0.024ms           |
| 20,000                       | 0.62ms          | 0.018ms           |   | 100                              | 8.07ms          | 0.12ms            |
| 30,000 (~ default list)      | 1.05ms          | 0.024ms           |   | 1,000                            | 78.42ms         | 0.84ms            |
| 300,000                      | 9.24ms          | 0.067ms           |   | 10,000                           | 800.94ms        | 5.59ms            |

> [!NOTE]
> All of these tests were conducted ~100 times and averaged. Tests were ran on an i9 13900k. Your results may vary, but relative performance should be a lot faster with the K-D Tree.

<br>

### Pretty notes ✨
While researching K-D trees, I put together some visuals to help me understand, and I figured why not make them pretty and provide it here. Hopfully these prove to be helpful for anyone interested in learning about K-D trees. And remember, there are plenty of other great resources out there (YouTube videos did it for me!).
![Untitled-2024-03-21-21563x](https://github.com/user-attachments/assets/33405c02-8fa0-4c89-8f18-13e011b6f717)
> [!NOTE]
> You can open that image in a new tab for a nicer, full-resolution view.

<br>

# Usage

## Importing

### Gradle.kts
```kts
repositories {
      maven("https://jitpack.io")
}

dependencies {
      implementation("me.aroze:color-names:1.0.2")
}
```

### Maven
```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>

<dependency>
  <groupId>me.aroze</groupId>
  <artifactId>color-names</artifactId>
  <version>1.0.2</version>
</dependency>
```

## Closest color

### Java
```java
public ColorNames colorNames = new ColorNameBuilder()
  .loadDefaults()
  .build();

String fromHex = colorNames.getName("#facfea"); // "Classic Rose"
String fromRGB = colorNames.getName(224, 224, 255); // "Stoic White"
String fromColor = colorNames.getName(new Color(255, 219, 240)); // "Silky Pink"
```

### Kotlin
```kt
val colorNames = ColorNameBuilder()
  .loadDefaults()
  .build()

val fromHex = colorNames.getName("#facfea") // "Classic Rose"
val fromRGB = colorNames.getName(224, 224, 255) // "Stoic White"
val fromColor = colorNames.getName(Color(255, 219, 240)) // "Silky Pink"
```

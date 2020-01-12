# HologramDisplays

Hologram API, a library to create holograms on Minecraft servers (1.8.8 ONLY)
**You must add the API as your project dependencies and add this to plugins folder**

## Features:
  - For now, only 1.8.8 because i'm using some reflections
  - You can create a hologram server-side means that every player on the server will see it
  - And also a client-side that means you can customize it for every player (e.g money, rank...)
  - No blinking for server-side, but some blink on client-side
  - Auto refresh
  - Using Java 8

## TODO:
  - Make it no blinking on client-side
  - Make it multi-version
  - Add ProtocolLib as dependencies for safe-use
  - Add Gradle/Maven dependences for this project

## Examples:

Initialize the HologramManager on your main class:
```java
HologramManager hologramManager; // Create a field to use on the whole project

@Override
public void onEnable(){
  this.hologramManager = new HologramManager(this); // Initialize the manager
}

@Override
public void onDisable(){
  hologramManager.clear(); // Clear all holograms created on this instance
}
```

Create a server-side hologram:
```java
// A list of Text for the hologram (dynamics variables)
List<Text> listText = new ArrayList<>();
listText.add(() -> "Hello everyone");
listText.add(() -> "I'm a server-side hologram");

// Create the hologram, if a hologram exist on this location, it will return the hologram on location
hologramManager.createServer(listText, location, refresh);
```

![Server-side](https://user-images.githubusercontent.com/28607056/72219392-ea693380-3545-11ea-9206-7c31e8ad8e4b.png)

Create a client-side hologram:
```java
// A list of Text for the hologram (dynamics variables)
List<Text> listText = new ArrayList<>();
listText.add(() -> "Hello " + player.getName());
listText.add(() -> "I'm a client-side hologram");

// Create the hologram for the player only!
hologramManager.createClient(player, listText, location, refresh);
```

Interact with the hologram:
```java
Hologram hologram = hologramManager.createServer(listText, location, refresh);

// When a player interact on the hologram, you can do somes actions!
hologram.interact(new Action(){
    @Override
    public void execute(Player player){
      player.sendMessage("hi there!");
  }
});
```

![Client-side](https://user-images.githubusercontent.com/28607056/72219393-ee955100-3545-11ea-8931-c1298c98dec8.png)

If you have a Hologram object, you can do with the HologramManager:
```java
// Teleport a hologram on new location
hologramManager.teleport(hologram, location);
        
// Get a hologram by using location block
hologramManager.getHologram(location.getBlock());
        
// Remove a hologram
hologramManager.remove(hologram);
```

## Downloads:

  ### Manual:
  Download the latest version on the [Releases](https://github.com/Watch54/HologramDisplays/releases).
  
  ### Gradle/Maven
  It will come later!

## Problems:
If you have somes issues with the API or if you have an idea, create an issue [here](https://github.com/Watch54/HologramDisplays/issues)

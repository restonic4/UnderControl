# Under control

<div align="center">
  <img src="src/main/resources/assets/under_control/icon.png" alt="Under control library icon" width="200" height="200">
</div>

Under Control is a library I developed primarily for my personal use. It brings together a variety of useful features to simplify certain aspects of development.

## What's Included?

- Robust Save System. Enables you to save data on both the client and worlds:
  - Save primitive types: Integer, Float, Boolean...
  - Supports saving custom classes, where you can specify how they should be saved.
- Configuration System. Built on the save system, compatible with multiple mods:
  - Easily add options.
  - On servers, the /reload command correctly reloads the configuration.
  - Option to add a custom configuration menu, compatible with:
    - ModMenu
    - More coming soon... Suggestions are welcome.
- Incompatibility Detection System. 
  - Checks if the client has specific mods installed to dynamically allow or deny compatibility with your mod.
  - Server compatible:
    - The server retrieves the list of mods the client has, including client-side mods.
    - Can be used as a simple security layer to prevent malicious mods on your server.
  - If you are the owner of a modpack, you can create a mod that prevents you from joining the server if you are using an outdated version of the modpack by using the function IncompatibilitiesAPI.setModPackRequiredVersion();
- Advanced Rendering System.
  - Easily register your own shaders.
  - Special effects:
    - Spheres.
    - Other geometric shapes.
    - More coming soon.
- Whitelist system.
  - Create whitelists of all kinds with specific conditions.
- New Events.
  - BlockEvents.
  - ClientEvents.
  - LivingEntityExtraEvents.
  - ServerPlayerExtraEvents.
  - ShaderEvents.
  - ClientLifeExtraEvents.
  - OtherEvents.
  - More coming soon.
- Dynamic Mixin System. Decides whether a mixin is applied based on specific conditions.
- Endpoint listening system for APIs with ApiClient.
- ServerAPI:
  - Redirect players to a different server.
  - Retrieve information about a server, such as its players, name, MOTD, etc.
- Easing system.
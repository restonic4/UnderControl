This file contains vague and probably wrong explanations for complex mixin behaviour. Because I could no find any official documentation talking about those complex methods and stuff.

# Opcodes

There is multiple types of Opcodes, such as GETFIELD, INVOKEVIRTUAL, etc.

I don't know what kind of dark magic this is using, people say it's related with the bytecode (I know what bytecode is, but how does this apply here? idk)

### GETFIELD

##### Source code
```java
public class Player {
    private int health;
    public void checkHealth() {
        if (this.health <= 0) {
            System.out.println("Chat, he died, no way! Clip that!");
        }
    }
}
```

##### Mixin
```java
@Inject(
        method = "checkHealth",
        at = @At(
                value = "FIELD",
                target = "LPlayer;health:I",
                opcode = Opcodes.GETFIELD
        )
)
private void onCheckHealth(CallbackInfo ci) {
    System.out.println("Accessing health variable!");
}
```

In this case, the mixin its inyected when the variable 'health' gets it's value loaded in.

### INVOKEVIRTUAL

##### Source code
```java
public class Zombie {
    public void attack() {
        Player target = findTarget();
        target.damage(5);
    }
}
```

##### Mixin
```java
@Inject(
        method = "attack",
        at = @At(
                value = "INVOKE",
                target = "LPlayer;damage(I)V",
                opcode = Opcodes.INVOKEVIRTUAL
        )
)
private void beforeDamage(CallbackInfo ci) {
    System.out.println("The zombie is about to damage the player, skill issue");
}
```

In this case, the mixin its inyected when the method 'damage' gets called.

### PUTSTATIC

##### Source code
```java
public class GameSettings {
    public static boolean isHardcore = false;

    public void toggleDifficulty() {
        isHardcore = !isHardcore;
    }
}
```

##### Mixin
```java
@Inject(
        method = "toggleDifficulty",
        at = @At(
                value = "FIELD",
                target = "LGameSettings;isHardcore:Z",
                opcode = Opcodes.PUTSTATIC
        )
)
private void onToggleHardcore(CallbackInfo ci) {
    System.out.println("Hardcore toggled");
}
```

When something is put on a static variable I guess
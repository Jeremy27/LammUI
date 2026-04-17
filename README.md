# LammUI

Bibliothèque de composants Swing pour la suite applicative **Lamm**. Identité visuelle sobre : bleu-marine en light, émeraude en dark, dégradé signature en header, inputs style Materialize.

## Stack

- Java 25
- Swing (aucune dépendance)
- Font Quicksand (système)

## Installation locale

```bash
mvn install
```

La lib est ensuite disponible pour les autres projets Lamm via :

```xml
<dependency>
    <groupId>fr.courel</groupId>
    <artifactId>lammui</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Composants

| Composant | Rôle |
|---|---|
| `LammFrame` | Fenêtre racine, gère le repaint sur changement de thème |
| `LammHeader` | Bandeau supérieur avec dégradé signature |
| `LammTitle` | Titre "Lamm" + suffixe light |
| `LammCard` | Panneau avec titre, barre accent gauche, séparateur |
| `LammButton` | Bouton raised, `.flat()`, `.accent()` — ripple effect |
| `LammTextField` | Champ texte Materialize (ligne animée, label flottant) |
| `LammTextArea` | Zone de texte avec label et bordure |
| `LammComboBox<E>` | Combo Materialize |
| `LammSpinner` | Spinner compact avec flèches custom |
| `LammCheckbox` | Case à cocher |
| `LammSwitch` | Toggle animé, `setOnGradient(true)` pour placement sur header |
| `LammTree` | Arborescence avec icônes file/folder |
| `LammScrollPane` | ScrollPane thémé |
| `LammProgressBar` | Barre animée, mode indéterminé, `setStringPainted` |
| `LammLabel` | Label avec styles `TITLE`, `SUBTITLE`, `BODY`, `CAPTION` |
| `LammDialog` | `info`, `warning`, `error`, `success`, `confirm` |

## Thème

`LammTheme` est une `sealed interface` (`Light`, `Dark`). Switch à chaud :

```java
LammTheme.toggle();
LammTheme.repaintAll(window);
```

Les composants s'abonnent via `LammTheme.onThemeChange(Runnable)`.

## Showcase

Un showcase de tous les composants est fourni :

```bash
mvn exec:java -Dexec.mainClass=fr.courel.lammui.LammShowcase \
              -Dexec.classpathScope=test
```

ou depuis l'IDE : lancer `src/test/java/fr/courel/lammui/LammShowcase.java`.

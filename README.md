# LammUI

Bibliothèque de composants UI pour la suite applicative **Lamm**. Couvre les piliers Swing (legacy) et **JavaFX** (cible long terme depuis la 2.0).

## Identité visuelle (LammUI 2.0+, JavaFX)

- Palette **warm cream** en light (`#F7F4EE` bg, `#FFFCF6` surface) / **slate** en dark (`#0F1622` / `#1E2638`)
- Accent par défaut **bleu acier** (`#4682B4` light, `#7AB1D6` dark luminescent)
- Presets d'accent dispos via classes sur `.root` : `.accent-emerald`, `.accent-amber`
- Cards **flat** avec ombre douce + radius 12 (fini la barre 2px à gauche)
- Header **inversé** : flat sur le bg de la page + ligne 2px d'accent en bas (fini le dégradé bannière)
- Stage `UNDECORATED` + chrome custom `LammChromeFx` (drag, min/max/close, slot d'actions custom)
- Inputs style **Materialize** : label flottant animé, ligne sous le champ qui change au focus

## Stack

- Java 25
- Swing (zéro dépendance) — composants legacy
- JavaFX 25 (`org.openjfx:javafx-controls:25`) — composants modernes

## Installation

LammUI est publiée sur **GitHub Packages**. Dans `~/.m2/settings.xml` :

```xml
<servers>
  <server>
    <id>github-lammui</id>
    <username>YOUR_GITHUB_USERNAME</username>
    <password>YOUR_PAT_WITH_READ_PACKAGES</password>
  </server>
</servers>
```

Puis dans le pom du projet consommateur :

```xml
<repositories>
  <repository>
    <id>github-lammui</id>
    <url>https://maven.pkg.github.com/Jeremy27/LammUI</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>fr.courel</groupId>
    <artifactId>lammui</artifactId>
    <version>2.0.2</version>
  </dependency>
</dependencies>
```

## Composants JavaFX (`fr.courel.lammui.fx`)

| Composant | Rôle |
|---|---|
| `LammChromeFx` | Chrome custom pour stage `UNDECORATED` : drag, double-clic maximize, min/max/close, `addAction(node)` pour insérer une cog réglages, helper `iconButton(icon, runnable)` et `settingsIcon()` |
| `LammHeaderFx` | "Lamm" bold + suffix, ligne d'accent en bas (style appliqué via CSS) |
| `LammCardFx` | Card avec titre optionnel, ombre douce, radius 12 |
| `LammButtonFx` | Bouton avec `.primary()` (acier) et `.accent()` (or). **Convention** : `primary` pour les actions répétables (Imprimer, Lancer scan), `accent` pour les actions one-shot/irréversibles (Supprimer N, Lancer release) |
| `LammTextFieldFx` | Champ texte Materialize (label flottant animé) |
| `LammProgressBarFx` | Barre fine 6px |
| `LammTreeFx<T>` + `LammTreeIcons` | TreeView stylé Lamm, icônes file/folder/folderOpen vectorielles |
| `LammSwitchFx` | Toggle animé (track + thumb) |
| `LammSpinnerFx` | Spinner numérique style Materialize |
| `LammThemeFx` | Light/Dark switch à chaud sur `Scene` |

Composants natifs JavaFX stylés via `lamm.css` (à utiliser tels quels) :
**ComboBox**, **CheckBox**, **RadioButton**, **ScrollPane**, **TextArea**, **Alert** / **Dialog**, **ContextMenu** — tous prennent automatiquement la palette Lamm.

## Composants Swing (`fr.courel.lammui.component`) — legacy

Toujours disponibles pour les apps Swing existantes. Identité visuelle d'origine (navy + émeraude + dégradé header).

| Composant | Rôle |
|---|---|
| `LammFrame` | Fenêtre racine, gère le repaint sur changement de thème |
| `LammHeader` | Bandeau supérieur avec dégradé signature |
| `LammTitle` | Titre "Lamm" + suffixe light |
| `LammCard` | Panneau avec titre, barre accent gauche, séparateur |
| `LammButton` | Bouton raised, `.flat()`, `.accent()` — ripple effect |
| `LammTextField` / `LammTextArea` | Champs Materialize |
| `LammComboBox<E>` | Combo Materialize |
| `LammSpinner` | Spinner compact avec flèches custom |
| `LammCheckbox` / `LammRadioButton` / `LammRadioGroup` | Inputs |
| `LammSwitch` | Toggle animé, `setOnGradient(true)` pour placement sur header |
| `LammTree` | Arborescence avec icônes file/folder |
| `LammScrollPane` | ScrollPane thémé |
| `LammWindowControls` | Boutons fenêtre custom (min/max/close) |
| `LammProgressBar` | Barre animée |
| `LammLabel` | Label avec styles `TITLE`, `SUBTITLE`, `BODY`, `CAPTION` |
| `LammDialog` | `info`, `warning`, `error`, `success`, `confirm` |

## Thème JavaFX

```java
LammThemeFx.install(scene);                    // applique lamm.css
LammThemeFx.setMode(scene, Mode.DARK);         // bascule
LammThemeFx.toggle(scene);                     // togglepub
```

Pour changer d'accent dynamiquement, ajouter/retirer la classe sur le scene root :

```java
scene.getRoot().getStyleClass().add("accent-emerald");  // ou accent-amber
// retirer "accent-*" pour revenir au default (steel)
```

## Thème Swing

```java
LammTheme.toggle();
LammTheme.repaintAll(window);
```

Les composants s'abonnent via `LammTheme.onThemeChange(Runnable)`.

## Showcase

- **JavaFX** : `src/test/java/fr/courel/lammui/fx/LammShowcaseFx.java` — runnable depuis l'IDE
- **Swing** : `src/test/java/fr/courel/lammui/LammShowcase.java`

## CI / release

Les workflows de publish vivent dans [LammCI](https://github.com/Jeremy27/LammCI). Le caller local invoque `library-release.yml` qui déploie sur GitHub Packages au tag `v*`.

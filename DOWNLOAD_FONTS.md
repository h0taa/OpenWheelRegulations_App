# Adding the Orbitron + Inter fonts

The app is designed to use **Orbitron** for headers / year numbers and **Inter**
for body text. To keep the project free of binary blobs, the actual `.ttf` files
are **not bundled**. Until you add them, the app falls back to the system default
font everywhere (it builds and runs fine — it just isn't using the brand fonts
yet). Adding them is a two-minute, two-step process.

Both families are free and open-source under the **SIL Open Font License 1.1**,
which permits bundling in apps.

## Step 1 — Get the font files

Either run the helper script from the project root:

```bash
./download_fonts.sh
```

…or download manually from Google Fonts and copy the files yourself:

- Orbitron: https://fonts.google.com/specimen/Orbitron
- Inter:    https://fonts.google.com/specimen/Inter

Place the files in `app/src/main/res/font/` using these **exact lowercase names**
(Android resource names must be lowercase, letters/digits/underscore only):

```
app/src/main/res/font/
├── orbitron_semibold.ttf
├── orbitron_bold.ttf
├── inter_regular.ttf
├── inter_medium.ttf
└── inter_semibold.ttf
```

If you download a "static" zip, the files inside are usually named like
`Orbitron-SemiBold.ttf` — just rename them to the snake_case names above.

## Step 2 — Switch the typography on

Open `app/src/main/java/com/openwheel/regulations/ui/theme/Type.kt`. Near the top
you'll find a commented block that defines `DisplayFontFamily` and
`BodyFontFamily` from the real font resources, and the two active lines that
currently point both at `FontFamily.Default`.

1. Uncomment the `FontFamily(Font(R.font.…))` definitions.
2. Delete (or comment out) the two `= FontFamily.Default` fallback lines.
3. Re-sync Gradle and run.

That's the whole change — the `Typography` further down already references
`DisplayFontFamily` and `BodyFontFamily`, so every text style updates at once.

## Why it's set up this way

Shipping fonts as committed binaries bloats the repo and complicates licensing
review. Keeping them out by default, with a one-command fetch and a single
swap-in point, keeps the source tree clean while making the brand look trivial to
enable. The OFL license text should be included in your About screen credits or a
`licenses/` folder when you do bundle them.

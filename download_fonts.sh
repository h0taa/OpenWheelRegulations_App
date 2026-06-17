#!/usr/bin/env bash
#
# Downloads Orbitron + Inter (SIL Open Font License 1.1) from the Google Fonts
# GitHub mirrors and drops them into app/src/main/res/font/ with the snake_case
# names the project expects. Safe to re-run.
#
# Requires: curl, unzip
#
set -euo pipefail

FONT_DIR="$(cd "$(dirname "$0")" && pwd)/app/src/main/res/font"
TMP_DIR="$(mktemp -d)"
trap 'rm -rf "$TMP_DIR"' EXIT

mkdir -p "$FONT_DIR"

echo "Downloading Orbitron + Inter into: $FONT_DIR"

# Orbitron (variable font on the Google Fonts repo; we extract the static-equivalent
# weights via fonttools if available, otherwise fall back to the variable file).
ORBITRON_URL="https://github.com/google/fonts/raw/main/ofl/orbitron/Orbitron%5Bwght%5D.ttf"
INTER_URL="https://github.com/google/fonts/raw/main/ofl/inter/Inter%5Bopsz,wght%5D.ttf"

curl -L --fail -o "$TMP_DIR/Orbitron-VF.ttf" "$ORBITRON_URL"
curl -L --fail -o "$TMP_DIR/Inter-VF.ttf" "$INTER_URL"

instance() {
  # instance <src-vf> <axis-spec> <dest>
  local src="$1" axes="$2" dest="$3"
  if command -v fonttools >/dev/null 2>&1; then
    fonttools varLib.instancer "$src" $axes -o "$dest" >/dev/null
  else
    # No fonttools: copy the variable font as-is. Android renders the default
    # instance; replace later with true static weights if you want exact weights.
    cp "$src" "$dest"
  fi
}

instance "$TMP_DIR/Orbitron-VF.ttf" "wght=600" "$FONT_DIR/orbitron_semibold.ttf"
instance "$TMP_DIR/Orbitron-VF.ttf" "wght=700" "$FONT_DIR/orbitron_bold.ttf"
instance "$TMP_DIR/Inter-VF.ttf"    "wght=400 opsz=14" "$FONT_DIR/inter_regular.ttf"
instance "$TMP_DIR/Inter-VF.ttf"    "wght=500 opsz=14" "$FONT_DIR/inter_medium.ttf"
instance "$TMP_DIR/Inter-VF.ttf"    "wght=600 opsz=14" "$FONT_DIR/inter_semibold.ttf"

echo "Done. Files written:"
ls -1 "$FONT_DIR"
echo
echo "Next: uncomment the real FontFamily definitions in Type.kt (see DOWNLOAD_FONTS.md)."
echo "Tip: 'pip install fonttools' gives you true per-weight static instances."

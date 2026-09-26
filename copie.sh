#!/bin/bash

OUTPUT_FILE="projet_complet.txt"

# Vider le fichier de sortie s'il existe déjà
> "$OUTPUT_FILE"

echo "=== CONCATÉNATION DU PROJET CORBA ===" >> "$OUTPUT_FILE"
echo "Date de génération : $(date)" >> "$OUTPUT_FILE"
echo "======================================" >> "$OUTPUT_FILE"
echo "" >> "$OUTPUT_FILE"

# Rechercher les fichiers pertinents (.java, .sh, .idl, .md) en ignorant bin et orb.db
find . \( -path "./bin" -o -path "./orb.db" \) -prune -o \
       \( -name "*.java" -o -name "*.sh" -o -name "*.idl" -o -name "*.md" \) -type f -print | sort | while read -r file; do
    
    # Ne pas inclure le fichier de sortie ou le script lui-même s'il est en .sh
    if [ "$file" != "./$OUTPUT_FILE" ]; then
        echo "==================================================" >> "$OUTPUT_FILE"
        echo "FICHIER : $file" >> "$OUTPUT_FILE"
        echo "==================================================" >> "$OUTPUT_FILE"
        cat "$file" >> "$OUTPUT_FILE"
        echo -e "\n\n" >> "$OUTPUT_FILE"
    fi
done

echo "Terminé ! Le contenu a été copié dans $OUTPUT_FILE"
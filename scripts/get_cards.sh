# Obtiene todas las tarjetas wifi del equipo
iwconfig --version | cut -d' ' -f1 | grep -v "iwconfig" | grep -v "Kernel" | grep -v "mon" | grep -v "^$"

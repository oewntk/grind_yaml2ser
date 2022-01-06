#!/bin/bash

IN="$1"
if [ -z "$1" ]; then
	IN=yaml
fi
echo "YAML:  ${IN}" 1>&2;

IN2="$2"
if [ -z "$2" ]; then
	IN2=yaml2
fi
echo "YAML2: ${IN2}" 1>&2;

OUTDIR="$3"
if [ -z "$3" ]; then
	OUTDIR=ser
fi
mkdir -p "${OUTDIR}"
echo "DIR:   "${OUTDIR}"" 1>&2;

OUTSER="$4"
if [ -z "$4" ]; then
	OUTSER=oewn.ser
fi

java -ea -jar oewn-grind-yaml2ser.jar "${IN}" "${IN2}" "${OUTDIR}/${OUTSER}"

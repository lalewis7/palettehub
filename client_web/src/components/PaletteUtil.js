export function convertColorsToArray(palette){
    let newPalette = {...palette}
    delete newPalette.color_1
    delete newPalette.color_2
    delete newPalette.color_3
    delete newPalette.color_4
    delete newPalette.color_5
    newPalette.colors = [palette.color_1, palette.color_2, palette.color_3, palette.color_4, palette.color_5]
    return newPalette
}

export function getTimeElapsed(timestamp){
    let elapsedSeconds = (Date.now() / 1000 - timestamp)
    if (elapsedSeconds < 60)
        return Math.floor(elapsedSeconds) + "s"
    else if (elapsedSeconds < 60 * 60)
        return Math.floor(elapsedSeconds / 60) + "m"
    else if (elapsedSeconds < 60 * 60 * 24)
        return Math.floor(elapsedSeconds / (60 * 60)) + "h"
    else if (elapsedSeconds < 60 * 60 * 24 * 30)
        return Math.floor(elapsedSeconds / (60 * 60 * 24)) + "d"
    else if (elapsedSeconds < 60 * 60 * 24 * 365)
        return Math.floor(elapsedSeconds / (60 * 60 * 24 * 30)) + "mo"
    else
        return Math.floor(elapsedSeconds / (60 * 60 * 24 * 365)) + "yr"
}
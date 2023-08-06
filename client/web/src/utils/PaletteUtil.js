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
    if (elapsedSeconds < 2)
        return "just now"
    else if (elapsedSeconds < 60)
        return Math.floor(elapsedSeconds) + "s ago"
    else if (elapsedSeconds < 60 * 60)
        return Math.floor(elapsedSeconds / 60) + "m ago"
    else if (elapsedSeconds < 60 * 60 * 24)
        return Math.floor(elapsedSeconds / (60 * 60)) + "h ago"
    else if (elapsedSeconds < 60 * 60 * 24 * 30)
        return Math.floor(elapsedSeconds / (60 * 60 * 24)) + "d ago"
    else if (elapsedSeconds < 60 * 60 * 24 * 365)
        return Math.floor(elapsedSeconds / (60 * 60 * 24 * 30)) + "mo ago"
    else
        return Math.floor(elapsedSeconds / (60 * 60 * 24 * 365)) + "yr ago"
}
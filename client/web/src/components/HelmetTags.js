import { Helmet } from "react-helmet";
import { useLocation } from "react-router-dom";

export default function HelmetTags(props){
    const location = useLocation()
    return <Helmet>
        {/* <!-- HTML Meta Tags --> */}
        <title>{props.title || "Palette Hub - Color palettes for web developers, artists, and color enthusiasts"}</title>
        <meta name="description" content={props.desc || "Palette Hub allows you to inspire your creativty and put it to use by browsing, curating, and sharing color palettes of any and all aesthetics."} />
        <meta name="keywords" content={props.keywords || "palette hub colors friends share"} />

        {/* <!-- Facebook Meta Tags --> */}
        <meta property="og:url" content={location.pathname && "https://palettehub.net"+location.pathname} />
        {/* <meta property="og:type" content={props.og_type || "website"} /> */}
        <meta property="og:title" content={props.title ? props.title : "123Palette Hub - Color palettes for web developers, artists, and color enthusiasts"} />
        <meta property="og:description" content={props.desc || "Palette Hub allows you to inspire your creativty and put it to use by browsing, curating, and sharing color palettes of any and all aesthetics."} />
        {/* <meta property="og:image" content={props.og_image || "/og.png"} /> */}

        {/* <!-- Twitter Meta Tags --> */}
        {/* <meta name="twitter:card" content={props.twiiter_card || "summary_large_image"} />
        <meta property="twitter:domain" content={props.twitter_domain || "palettehub.net"} /> */}
        <meta property="twitter:url" content={props.og_url || "https://palettehub.net"} />
        <meta name="twitter:title" content={props.title || "Palette Hub - Color palettes for web developers, artists, and color enthusiasts"} />
        <meta name="twitter:description" content={props.desc || "Palette Hub allows you to inspire your creativty and put it to use by browsing, curating, and sharing color palettes of any and all aesthetics."} />
        {/* <meta name="twitter:image" content={props.og_image || "/og.png"}></meta>         */}
    </Helmet>
}
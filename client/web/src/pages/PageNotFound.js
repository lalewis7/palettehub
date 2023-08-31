import HelmetTags from "components/HelmetTags";
import { Button } from "react-bootstrap";
import { ExclamationTriangle } from "react-bootstrap-icons";
import { Link } from "react-router-dom";

export default function PageNotFound(){
    const containerStyle = {
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        paddingTop: "2rem",
        width: "100%"
    }
    return <>
        <HelmetTags title="404 Error - Palette Hub" />
        <div 
        // @ts-ignore
        style={containerStyle}>
            <h2 className="display-2">404</h2>
            <h5 className="mb-4">Page Not Found</h5>
            <Link to="/"><Button>Return to homepage</Button></Link>
        </div>
    </>
}
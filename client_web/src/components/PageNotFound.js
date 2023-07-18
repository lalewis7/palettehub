import { Button } from "react-bootstrap";
import { ExclamationTriangle } from "react-bootstrap-icons";
import { Link } from "react-router-dom";

export default function PageNotFound(){
    const containerStyle = {
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        paddingTop: "2rem"
    }
    return <>
        <div 
        // @ts-ignore
        style={containerStyle}>
            <ExclamationTriangle size={96} />
            <h1><b>404</b></h1>
            <h4 className="mb-4">Page Not Found</h4>
            <Link to="/"><Button>Return to homepage</Button></Link>
        </div>
    </>
}
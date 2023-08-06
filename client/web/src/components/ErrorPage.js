import { Button } from "react-bootstrap";
import { ExclamationTriangle } from "react-bootstrap-icons";
import { Link } from "react-router-dom";

export default function ErrorPage(props){
    const containerStyle = {
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        paddingTop: "2rem",
        width: "100%"
    }
    return <>
        <div 
        // @ts-ignore
        style={containerStyle}>
            <ExclamationTriangle size={72} />
            <h1 className="display-5">Error {props.code}</h1>
            {props.msg ? <p className="mb-4">{props.msg}</p> : ''}
            {props.retry || false ? <Button onClick={props.retry}>Retry</Button> :
            <Link to="/"><Button>Return to homepage</Button></Link>}
        </div>
    </>
}
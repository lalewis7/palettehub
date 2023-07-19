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
            <ExclamationTriangle size={96} />
            <h1>Error <b>{props.code}</b></h1>
            {props.msg ? <h4 className="mb-4">{props.msg}</h4> : ''}
            {props.retry || false ? <Button onClick={props.retry}>Retry</Button> :
            <Link to="/"><Button>Return to homepage</Button></Link>}
        </div>
    </>
}